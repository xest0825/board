package kr.devsnote.log;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class LogDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Log.";

    /**
     * 액션 이력 입력
     *
     * @param
     * @return
     */
    public int insertActionLog(HashMap<String, String> paramMap) {
        return sqlSession.insert(SQL_PREFIX + "insertActionLog", paramMap);
    }

    /**
     * 오류 이력 입력
     *
     * @param
     * @return
     */
    public int insertErrorLog(HashMap<String, String> paramMap){
        return sqlSession.insert(SQL_PREFIX + "insertErrorLog", paramMap);
    }




}
