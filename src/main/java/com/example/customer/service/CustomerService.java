package com.example.customer.service;

import com.example.customer.dao.CustomerDAO;
import com.example.customer.vo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO dao;

    public List<HashMap<String, Object>> getCustomerList(Customer vo){
        return dao.getCustomerList(vo);
    }

    public int getCustomerListCount(Customer vo){
        int ret = 0;
        List<HashMap<String, Object>> list = dao.getCustomerList(vo);
        if (list.size() > 0) {
            ret = list.size();
        }
        return ret;
    }

    public int insertCustomer(Customer vo) {
        return dao.insertCustomer(vo);
    }

    public int updateCustomer(Customer vo) {
        return dao.updateCustomer(vo);
    }

    public int deleteCustomer(Customer vo) {
        return dao.deleteCustomer(vo);
    }
}
