package com.example.code.controller;

import com.example.code.service.CodeService;
import com.example.code.vo.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class CodeRestController {

    @Autowired
    private CodeService service;

    @GetMapping("/codes")
    public ResponseEntity<List<HashMap<String, Object>>> getBoardItems() {
        log.info("[GET] /codes");
        List list = new ArrayList<HashMap<String, Object>>();
        Code vo = new Code();
        list = service.getCodeList(vo);

        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    };


    @PostMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> insertItems(@RequestBody Code model) {
        log.info("[POST] /codes");

        HashMap map = new HashMap<String, Object>();
        if (service.insertCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;

    };

    @PutMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> updateItems(@RequestBody Code model) {
        log.info("[PUT] /codes");

        HashMap map = new HashMap<String, Object>();
        if (service.updateCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @DeleteMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> deleteItems(@RequestBody Code model) {
        log.info("[DELETE] /codes");

        String cd = model.getCd();
        log.info(cd);

        HashMap map = new HashMap<String, Object>();
        if (service.deleteCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

}
