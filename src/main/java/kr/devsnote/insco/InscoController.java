package kr.devsnote.insco;

import kr.devsnote.insco.InscoService;
import kr.devsnote.insco.InscoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class InscoController {

    @Autowired
    private InscoService service;

    @GetMapping("/inscos")
    public ResponseEntity<List<HashMap<String, Object>>> getInscoList(InscoVO vo) {
        log.info("/inscos");
        List<HashMap<String, Object>> retList = service.getInscoList(vo);
        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(retList, HttpStatus.OK);
        return ret;
    };



}
