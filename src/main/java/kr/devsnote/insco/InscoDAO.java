package kr.devsnote.insco;

import kr.devsnote.insco.InscoVO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class InscoDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Insco.";

    public List<HashMap<String, Object>> getInscoList(InscoVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getInscoList", vo);
    }

    public int insertInsco(InscoVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertInsco", vo);
    }

    public int updateInsco(InscoVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateInsco", vo);
    }

    public int deleteInsco(InscoVO vo) {
        return sqlSession.update(SQL_PREFIX + "deleteInsco", vo);
    }


}
