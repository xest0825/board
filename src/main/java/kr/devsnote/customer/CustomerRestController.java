package kr.devsnote.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
public class CustomerRestController {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers/{cust_id}")
    public ResponseEntity<HashMap<String, Object>> getCustomer(CustomerVO vo, @PathVariable String cust_id) {
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
    public ResponseEntity<HashMap<String, Object>> get(CustomerVO vo) {
        log.info("/customer-id-count");
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("customer_id_cnt", service.getCustomerListCount(vo));
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @PostMapping("/customers")
    public ResponseEntity<HashMap<String, Object>> insertItems(@RequestBody CustomerVO vo) {
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
    public ResponseEntity<HashMap<String, Object>> updateItems(@RequestBody CustomerVO model) {


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
    public ResponseEntity<HashMap<String, Object>> deleteItems(@RequestBody CustomerVO model) {


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
