package kr.devsnote.exception;

import kr.devsnote.config.Constants;
import kr.devsnote.log.LogService;
import kr.devsnote.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * ExceptionHandler 
 * 예외처리  ExceptionResolver
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandler extends SimpleMappingExceptionResolver  {

	@Autowired
	private LogService logsService;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){

		StackTraceElement[] st = ex.getStackTrace();
		StringBuilder sb = new StringBuilder();

		for(int i=0;i<st.length;i++)
		{
			sb.append(st[i]);
			sb.append("\n");
			if (i==4)break;
		}

		log.error("");
		log.error("##########################");
		log.error("#call  doResolveException#");
		log.error("request.getRequestURI = {}",request.getRequestURI());
		log.error("response.getStatus() = {}",response.getStatus() );
		log.error("ex.getClass()        = {}",ex.getClass().toString()        );
		log.error("ex.getMessage()      = {}",ex.getMessage()      );
		log.error("ex.toString()        = {}",ex.toString()        );
		log.error("StackTrace  = ");
		log.error("{}", sb);
		log.error("##########################");
		log.error("");
			
		//에러 로그 저장
		try {
		
			Constants.LOGGING QueryLogging = Constants.QueryLogging;
			// Constants.QueryLogging = Constants.LOGGING.NOLOGGING;

	        HashMap<String, String> errMap = new HashMap<>();
	        errMap.put("err_url", "["+request.getMethod() +"]" + request.getRequestURI());
	        errMap.put("err_sts", String.valueOf(response.getStatus())); 			/* VARCHAR(10)  	'오류 상태 (코드)' 	*/
	        errMap.put("err_cls", ex.getClass().toString()); 			/* VARCHAR(200) 	'오류 발생 클래스' 	*/
	        errMap.put("err_msg", ex.getMessage()); 			/* VARCHAR(4000)  	'오류 메시지' 		*/
	        errMap.put("err_trc", sb.toString()); 			/* VARCHAR(4000)  	'오류 트레이스' 	*/
	       
	        errMap.put("user_id", ""); 			/* VARCHAR(32)  	'사용자 ID' 		*/
			
			Enumeration paramsss = request.getParameterNames();
			System.out.println("---------- request params @ exception handler ----------");
			String params = "";
			while (paramsss.hasMoreElements()){
			    String name = (String)paramsss.nextElement();
			    params += ( name + "=" + request.getParameter(name) + "&");
			    log.debug(name + " : " +request.getParameter(name));
			}
			errMap.put("params", params);
			
			// HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			errMap.put("err_env", request.getHeader("User-Agent"));
			errMap.put("error_ip", CommonUtil.getClientIP(request));
			errMap.put("system_gubun", "board");
			errMap.put("action_type", "");

			int i = logsService.insertErrorLog(errMap);
			Constants.QueryLogging = QueryLogging;
		
		} catch (Exception e1) {
			log.error(e1.getMessage());
		}
		
		String exmsg = "";
		
		// TODO: 익센셥에 대한 메시지 정의를 구현한다.
		if(ex instanceof DuplicateKeyException ){
			exmsg = "중복된 데이타가 있습니다.";
		}
		else if(ex instanceof DataIntegrityViolationException){
		    DataIntegrityViolationException bEx = ((DataIntegrityViolationException) ex);
            
            log.error("bEx.getErrorCode() = {}",bEx.getLocalizedMessage());
        
            // 개발편의를 위해서 에러 메세지 출력
            //exmsg = "데이타 처리중 에러가 발생하였습니다.";
            exmsg = bEx.getLocalizedMessage();
            exmsg = exmsg.replaceAll("ORA", "<br/>ORA");
            //exmsg = exmsg.substring(exmsg.indexOf(":")+1, exmsg.indexOf("\n"));
            
		}
		else if(ex instanceof UncategorizedSQLException){
		    SQLException bEx = ((UncategorizedSQLException) ex).getSQLException();
	            
            log.error("bEx.getErrorCode() = {}",bEx.getErrorCode());
            
            if(bEx.getErrorCode() > 20000)
            {
                exmsg = bEx.getMessage();
                exmsg = exmsg.substring(exmsg.indexOf(":")+1, exmsg.indexOf("\n"));
            }
            else
            {
                // 개발편의를 위해서 에러 메세지 출력
                //exmsg = "데이타 처리중 에러가 발생하였습니다.";
                exmsg = bEx.getMessage();
                exmsg = exmsg.substring(exmsg.indexOf(":")+1, exmsg.indexOf("\n"));
            }
		}
		else if(ex instanceof SQLException  ){
		    SQLException sqlEx = (SQLException)ex;
		    
		    log.error("sqlEx.getErrorCode() = {}",sqlEx.getErrorCode());
            if(sqlEx.getErrorCode() > 20000)
            {
                exmsg = sqlEx.getMessage();
                exmsg = exmsg.substring(exmsg.indexOf(":")+1, exmsg.indexOf("\n"));
            }
            else
            {
                // 개발편의를 위해서 에러 메세지 출력
                //exmsg = "데이타 처리중 에러가 발생하였습니다.";
                exmsg = sqlEx.getMessage();
                exmsg = exmsg.substring(exmsg.indexOf(":")+1, exmsg.indexOf("\n"));
            }
		    
			exmsg = "SQL 에러가 발생하였습니다.";
		}else if(ex instanceof DataAccessException){
			exmsg = "데이타 처리중 에러가 발생하였습니다.";
		}else if(ex instanceof NullPointerException ){
			exmsg = "필요 인자가 없어 에러가 발생하였습니다.";
		}else if(ex instanceof InvalidFormatException){
		    exmsg = "xlsx 형식에 엑셀파일만 지원합니다.";
		}else{
			exmsg = ex.getMessage();
		}

		log.info("exmsg : " + exmsg);
		
		//ajax url은 json으로 에러 헨들링
		if(request.getRequestURI().toLowerCase().indexOf(".ajax") > 0 || "XMLHttpRequest".contentEquals(request.getHeader("x-requested-with"))){
			log.info("CASE AJAX");
			JSONObject rtn = new JSONObject();
			rtn.put("errmsg",  exmsg);
			
			try{
				CommonUtil.sendjson(response, rtn , 500);
				return null;
			} catch (Exception e) {
				log.error(exmsg);
			}
			
		 } else {
			log.info("CASE NOT AJAX");
			ModelAndView model = new ModelAndView();
			model.setViewName("/error");
			return model;
		}
		
		return null;
	}
	

		
}