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
        return sqlSession.selectList(SQL_PREFIX + "getCodeList", vo);
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

    public List<HashMap<String, Object>> getGroupCodeList(Code vo) {
        return sqlSession.selectList(SQL_PREFIX + "getGroupCodeList", vo);
    }

    public int insertGroupCode(Code vo) {
        return sqlSession.insert(SQL_PREFIX + "insertGroupCode", vo);
    }

    public int updateGroupCode(Code vo) {
        return sqlSession.update(SQL_PREFIX + "updateGroupCode", vo);
    }

    public int deleteGroupCode(Code vo) {
        return sqlSession.update(SQL_PREFIX + "deleteGroupCode", vo);
    }

}
