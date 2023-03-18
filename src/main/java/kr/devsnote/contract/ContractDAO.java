package kr.devsnote.contract;

import kr.devsnote.insco.InscoVO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ContractDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Contract.";

    public List<HashMap<String, Object>> getContractList(ContractVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getContractList", vo);
    }

    public int insertContract(ContractVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertContract", vo);
    }

    public int updateContract(ContractVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateContract", vo);
    }

    public int deleteContract(ContractVO vo) {
        return sqlSession.delete(SQL_PREFIX + "deleteContract", vo);
    }

}
