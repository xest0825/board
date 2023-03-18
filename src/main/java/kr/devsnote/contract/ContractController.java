package kr.devsnote.contract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ContractController {

    @Autowired
    private ContractService service;

    @GetMapping("/contracts")
    public ResponseEntity<List<HashMap<String, Object>>> getContractList(ContractVO vo) {
        log.info("/contracts");
        List<HashMap<String, Object>> retList = service.getContractList(vo);
        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(retList, HttpStatus.OK);
        return ret;
    };


}
