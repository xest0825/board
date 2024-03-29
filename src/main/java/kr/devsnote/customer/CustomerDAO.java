package kr.devsnote.customer;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CustomerDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    private static final String SQL_PREFIX = "Customer.";

    public List<HashMap<String, Object>> getCustomerList(CustomerVO vo) {
        return sqlSession.selectList(SQL_PREFIX + "getCustomerList", vo);
    }

    public int insertCustomer(CustomerVO vo) {
        return sqlSession.insert(SQL_PREFIX + "insertCustomer", vo);
    }

    public int updateCustomer(CustomerVO vo) {
        return sqlSession.update(SQL_PREFIX + "updateCustomer", vo);
    }

    public int deleteCustomer(CustomerVO vo) {
        return sqlSession.update(SQL_PREFIX + "deleteCustomer", vo);
    }


}
