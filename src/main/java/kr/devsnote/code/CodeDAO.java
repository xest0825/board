package kr.devsnote.code;

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

    public List<HashMap<String, Object>> getCodeList(CodeVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getCodeList", vo);
    }

    public int insertCode(CodeVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertCode", vo);
    }

    public int updateCode(CodeVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateCode", vo);
    }

    public int deleteCode(CodeVO vo) {
        return sqlSession.update(SQL_PREFIX + "deleteCode", vo);
    }

    public List<HashMap<String, Object>> getGroupCodeList(CodeVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getGroupCodeList", vo);
    }

    public int insertGroupCode(CodeVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertGroupCode", vo);
    }

    public int updateGroupCode(CodeVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateGroupCode", vo);
    }

    public int deleteGroupCode(CodeVO vo) {
        return sqlSession.update(SQL_PREFIX + "deleteGroupCode", vo);
    }

}
