package kr.devsnote.intercepter;

import kr.devsnote.config.Constants;
import kr.devsnote.config.Constants.LOGGING;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Query Interceptor
 * @author choiys
 * @Desc 쿼리를 로그에 남기기 위한 인터셉터
 *  참고로 인터셉터는 스프링 기술 스택
 */
@Slf4j
@Intercepts
(
    {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
       ,@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
       ,@Signature(type = Executor.class, method = "query",  args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
    }
)
public class QueryInterceptor implements Interceptor
{
    
    @Override
	@SuppressWarnings("rawtypes")
    //@Override
    public Object intercept(Invocation invocation) throws Throwable
    {
        if (Constants.QueryLogging == LOGGING.NOLOGGING )
        {   // 쿼리로그 비활성화시 return
            return invocation.proceed();
        }
        Object[]        args     = invocation.getArgs();
        MappedStatement ms       = (MappedStatement)args[0];
        Object          param    = args[1];
        BoundSql        boundSql = ms.getBoundSql(param);

        //StatementHandler handler = (StatementHandler) invocation.getTarget();
        
        //String paramStr = handler.getParameterHandler().getParameterObject() != null ? handler.getParameterHandler().getParameterObject().toString() : "";
        
        // 쿼리문을 가져온다(이 상태에서의 쿼리는 값이 들어갈 부분에 ?가 있다)
        String sql = boundSql.getSql();
        try 
        {
            if(param == null){              // 파라미터가 아무것도 없을 경우
                sql = sql.replaceFirst("\\?", "''");
            }
            else
            {   // 해당 파라미터의 클래스가 Integer, Long, Float, Double 클래스일 경우
                if(param instanceof Integer || param instanceof Long || param instanceof Float || param instanceof Double)
                {
                    sql = sql.replaceFirst("\\?", param.toString());
                }
                else if(param instanceof String)
                {   // 해당 파라미터의 클래스가 String 일 경우(이 경우는 앞뒤에 '(홑따옴표)를 붙여야해서 별도 처리
                    sql = sql.replaceFirst("\\?", "'" + param + "'");
                }
                else {
                    List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
                    if(param instanceof Map)
                    {   // 해당 파라미터가 Map 일 경우

                        /*
                         * 쿼리의 ?와 매핑되는 실제 값들의 정보가 들어있는 ParameterMapping 객체가 들어간 List 객체로 return이 된다.
                         * 이때 List 객체의 0번째 순서에 있는 ParameterMapping 객체가 쿼리의 첫번째 ?와 매핑이 된다
                         * 이런 식으로 쿼리의 ?과 ParameterMapping 객체들을 Mapping 한다
                         */

                        log.debug(param.toString());

                        for(ParameterMapping mapping : paramMapping)
                        {
                            String propValue = mapping.getProperty(); // 파라미터로 넘긴 Map의 key 값이 들어오게 된다
                            Object value = ((Map) param).get(propValue); // 넘겨받은 key 값을 이용해 실제 값을 꺼낸다
                            if (value instanceof String)
                            { // SQL의 ? 대신에 실제 값을 넣는다. 이때 String 일 경우는 '를 붙여야 하기땜에 별도 처리
                                sql = sql.replaceFirst("\\?", "'" + ((String) value).replaceAll("\\$", "\\\\\\$") + "'");
                            }
                            else
                            {
                                sql = sql.replaceFirst("\\?", (value == null?"'null'":value.toString()));
                            }

                        }

                    }
                    else
                    {
                            // 해당 파라미터가 사용자 정의 클래스일 경우
                            /*
                             * 쿼리의 ?와 매핑되는 실제 값들이 List 객체로 return이 된다.
                             * 이때 List 객체의 0번째 순서에 있는 ParameterMapping 객체가 쿼리의 첫번째 ?와 매핑이 된다
                             * 이런 식으로 쿼리의 ?과 ParameterMapping 객체들을 Mapping 한다
                             */

                        Class<?> paramClass = param.getClass();
                        // log.debug("paramClass.getName() : {}", paramClass.getName());
                            for(ParameterMapping mapping : paramMapping)
                            {
                                String propValue = mapping.getProperty();           // 해당 파라미터로 넘겨받은 사용자 정의 클래스 객체의 멤버변수명
                                Field field;

                                try {
                                    field = paramClass.getDeclaredField(propValue);   // 관련 멤버변수 Field 객체 얻어옴
                                }catch(NoSuchFieldException nsfe) {
                                    field = paramClass.getSuperclass().getDeclaredField(propValue); // MB_ID 같이 부모(BaseVO)에 있는 변수들을 얻어옴
                                }

                                field.setAccessible(true);                  // 멤버변수의 접근자가 private일 경우 reflection을 이용하여 값을 해당 멤버변수의 값을 가져오기 위해 별도로 셋팅
                                Class<?> javaType = mapping.getJavaType();          // 해당 파라미터로 넘겨받은 사용자 정의 클래스 객체의 멤버변수의 타입

                                // log.debug("javaType  " + javaType);
                                if(String.class == javaType)
                                {   // SQL의 ? 대신에 실제 값을 넣는다. 이때 String 일 경우는 '를 붙여야 하기땜에 별도 처리
                                    //log.debug("field : " + field + ", param :" + param + ", field.getName : " + field.getName());
                                    String filedVal = "";
                                    if (field.get(param) != null){
                                        filedVal = field.get(param).toString();
                                    }
                                    sql = sql.replaceFirst("\\?", "'" + filedVal.replaceAll("\\$", "\\\\\\$") + "'");
                                }
                                else
                                {
                                    sql = sql.replaceFirst("\\?", field.get(param).toString());
                                }
                            }
                    }
                }
                
            }
        } catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
        } finally{

        	log.debug("====================================");
        	log.debug("Method=[{}], ID=[{}]",invocation.getMethod().getName(),ms.getId());
        	log.debug("Query\n{}",sql);
            if(param != null) log.debug("{}", param);
            log.debug("====================================");
        }
        
        return invocation.proceed();
    }

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

}


