package com.example.customer.controller;

import com.example.board.service.BoardService;
import com.example.board.vo.Board;
import com.example.customer.service.CustomerService;
import com.example.customer.vo.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers/{cust_id}")
    public ResponseEntity<HashMap<String, Object>> getCustomer(Customer vo, @PathVariable String cust_id) {
        log.info("/customers/" + cust_id);
        vo.setCust_id(cust_id);
        HashMap<String, Object> map = service.getCustomer(vo);
        if (map.get("cust_id") != null) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @GetMapping("/customer-id-count")
    public ResponseEntity<HashMap<String, Object>> get(Customer vo) {
        log.info("/customer-id-count");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customer_id_cnt", service.getCustomerListCount(vo));
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @PostMapping("/customers")
    public ResponseEntity<HashMap<String, Object>> insertItems(@RequestBody Customer vo) {
        log.info("[POST] /customers");
        log.info("Customers ::: " + vo.toString());

        HashMap map = new HashMap<String, Object>();
        if (service.insertCustomer(vo) != -1) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @PutMapping("/customers")
    public ResponseEntity<HashMap<String, Object>> updateItems(@RequestBody Customer model) {


        HashMap map = new HashMap<String, Object>();
        if (service.updateCustomer(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @DeleteMapping("/customers")
    public ResponseEntity<HashMap<String, Object>> deleteItems(@RequestBody Customer model) {


        HashMap map = new HashMap<String, Object>();
        if (service.deleteCustomer(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

}
