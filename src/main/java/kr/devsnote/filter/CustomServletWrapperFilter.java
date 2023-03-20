package kr.devsnote.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import kr.devsnote.config.SecurityConstants;
import kr.devsnote.log.LogService;
import kr.devsnote.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

@Slf4j
@Component
public class CustomServletWrapperFilter extends OncePerRequestFilter {

	@Autowired
	LogService logService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		//필터에서는 request , response 객체를 변경할 수 있다.
		ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);
		String reqContent = new String(wrappingRequest.getContentAsByteArray());
		wrappingRequest.setAttribute("reqbd",reqContent);
		chain.doFilter(wrappingRequest, wrappingResponse);
		wrappingResponse.copyBodyToResponse();
		
		HashMap<String, String> paramap = new HashMap<String, String>();
		
		String url = wrappingRequest.getRequestURI();
		
		if (!url.contains("resources/") && !url.contains("META-INF/") && !url.contains("/index")
				&& !url.contains("/assets") && !url.contains("/font") && !url.contains("/js/") && !url.contains("/css/")
				&& !url.contains("/img/") && !url.contains("/images/") && !url.contains("/lib/")
				&& !url.contains("/swagger-ui/") && !url.contains("/swagger-resources/") && !url.contains("/v2/")
				&& !url.contains("/pdfjs/") 
				&& !url.contains("favicon")) {
			
			log.info(">>>> CustomServletWrapperFilter.doFilterInternal");
			paramap.put("action_url", url);
			paramap.put("action_method", wrappingRequest.getMethod());
			
			String params = "";
			Enumeration<String> paramsss = request.getParameterNames();
			log.info("----- request params @ CustomServletWrapperFilter -----");
			while (paramsss.hasMoreElements()) {
				String name = (String) paramsss.nextElement();
				params += (name + "=" + request.getParameter(name) + "&");
			}
			log.info(params);
			log.info("--------------------------------------------------------");
			String reqBody = new String(wrappingRequest.getContentAsByteArray());
			if (CommonUtil.isNotEmpty(reqBody)) {
				paramap.put("params", params + "&requestbody=" + reqBody);
				log.info("reqBody : " + reqBody);
			} else {
				paramap.put("params", params);			
			}
			
			String user_id = "";
			String authorization = request.getHeader("Authorization");

			if (CommonUtil.isNotEmpty(authorization)) {
				String token = authorization.substring(6);
				byte[] signingkey = SecurityConstants.JWT_SECRET.getBytes();
				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingkey).parseClaimsJws(token);
				user_id = parsedToken.getBody().getSubject();
			} else {
				authorization = request.getParameter("Authorization");
				if (CommonUtil.isNotEmpty(authorization)) {
					String token = authorization.substring(6);
					byte[] signingkey = SecurityConstants.JWT_SECRET.getBytes();
					Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingkey).parseClaimsJws(token);
					user_id = parsedToken.getBody().getSubject();
				} else {
					log.info("no authorization");
				}
			}

			paramap.put("user_id", user_id);
			paramap.put("user_ip", CommonUtil.getClientIP(wrappingRequest));
			paramap.put("action_env", wrappingRequest.getHeader("User-Agent"));
			paramap.put("sys_typ", "ADM");
			paramap.put("memo", "");
	
			logService.insertActionLog(paramap);
		}
		
		
	}

}
