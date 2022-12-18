package kr.devsnote.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO dao;

    public List<HashMap<String, Object>> getCustomerList(CustomerVO vo){
        return dao.getCustomerList(vo);
    }

    public int getCustomerListCount(CustomerVO vo){
        int ret = 0;
        List<HashMap<String, Object>> list = dao.getCustomerList(vo);
        if (list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public HashMap<String, Object> getCustomer(CustomerVO vo){
        int ret = 0;
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = dao.getCustomerList(vo);
        if (list.size() > 0) {
            retMap = list.get(0);
        }
        return retMap;
    }

    public int insertCustomer(CustomerVO vo) {
        return dao.insertCustomer(vo);
    }

    public int updateCustomer(CustomerVO vo) {
        return dao.updateCustomer(vo);
    }

    public int deleteCustomer(CustomerVO vo) {
        return dao.deleteCustomer(vo);
    }
}
