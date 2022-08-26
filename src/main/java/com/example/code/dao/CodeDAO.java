package com.example.code.dao;

import com.example.code.vo.Code;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CodeDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Code.";

    public List<HashMap<String, Object>> getCodeList(Code vo) {
        return sqlSession.selectList(SQL_PREFIX + "getICodeList", vo);
    }

    public int insertCode(Code vo) {
        return sqlSession.insert(SQL_PREFIX + "insertCode", vo);
    }

    public int updateCode(Code vo) {
        return sqlSession.update(SQL_PREFIX + "updateCode", vo);
    }

    public int deleteCode(Code vo) {
        return sqlSession.update(SQL_PREFIX + "deleteCode", vo);
    }


}
