package kr.devsnote.intercepter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import kr.devsnote.config.SecurityConstants;
import kr.devsnote.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class InterceptorAdapter extends HandlerInterceptorAdapter {

	private SqlSessionTemplate sqlSession;

	public InterceptorAdapter(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		if (request.getClass().getName().contains("SecurityContextHolderAwareRequestWrapper")) {
			log.info("SecurityContextHolderAwareRequestWrapper pass ...");
			// return true;
		}
		*/

		// session검사
		String url = request.getRequestURI();
		String method = request.getMethod();

        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest)request);
		//ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);

		if (!url.contains("resources/") && !url.contains("META-INF/") && !url.contains("/index")
				&& !url.contains("/asstes") && !url.contains("/font") && !url.contains("/js/") && !url.contains("/css/")
				&& !url.contains("/img/") && !url.contains("/images/") && !url.contains("/lib/")
				&& !url.contains("/swagger-ui/") && !url.contains("/swagger-resources/") && !url.contains("/v2/")
				&& !url.contains("favicon")) {
			log.info("================================== >>>");
			String reqContent = new String(httpServletRequest.getContentAsByteArray());
            log.info("InterceptorAdapter.preHandle request: {} {}, reqBody: {}", "[" + method + "]", url, reqContent);
			String reqbd = httpServletRequest.getAttribute("reqbd").toString();
			log.info("InterceptorAdapter.preHandle request: {} {}, reqbd: {}", "[" + method + "]", url, reqbd);
			// 인터셉터에서는 reqBody가 출력이 안됨.
			String params = "";

			Enumeration<String> paramsss = request.getParameterNames();
			log.info("----- requests params @ interceptor -----");
			while (paramsss.hasMoreElements()) {
				String name = (String) paramsss.nextElement();
				params += (name + "=" + request.getParameter(name) + "&");
			}
			log.info(params);
			log.info("-----------------------------------------");

			HashMap<String, String> map = new HashMap<String, String>(); // 로깅용 map

			// user_id 는 Header에 Authorization이 있을때 파싱하여 사용
			String user_id = "";
			String authorization = request.getHeader("Authorization");
			if (CommonUtil.isNotEmpty(authorization)) {

				String header = authorization;
				// log.info("header : " + header);
				String token = header.substring(6);

				// log.info("token : " + token);

				byte[] signingkey = SecurityConstants.JWT_SECRET.getBytes();
				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingkey).parseClaimsJws(token);

				// log.info("parsedToken : " + parsedToken);

				String username = parsedToken.getBody().getSubject();
				log.info("user_id : " + username);

				user_id = username;

			} else {
				authorization = request.getParameter("Authorization");
				if (CommonUtil.isNotEmpty(authorization)) {

					String header = authorization;
					// log.info("header : " + header);
					String token = header.substring(6);

					// log.info("token : " + token);

					byte[] signingkey = SecurityConstants.JWT_SECRET.getBytes();
					Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingkey).parseClaimsJws(token);

					// log.info("parsedToken : " + parsedToken);

					String username = parsedToken.getBody().getSubject();
					log.info("user_id : " + username);

					user_id = username;

				} else {
					log.info("no authorization");

				}

			}

			HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String ua = request.getHeader("User-Agent");

			String reqBody = "";
			map.put("action_url", url);
			map.put("action_method", method);
			map.put("user_id", user_id);
			map.put("user_ip", CommonUtil.getClientIP(req));
			map.put("action_env", ua);
			map.put("params", params + reqBody);
			map.put("sys_typ", "ADM");
			log.info(map.toString());

			// sqlSession.insert("Log." + "insertActionLog", map);
			log.info("<<< ==================================");


		}


		return true;
	}
	
	@Override
    public void afterCompletion(
            HttpServletRequest request, 
            HttpServletResponse response, 
            Object handler,
            Exception ex) throws Exception {
		
	}


}
