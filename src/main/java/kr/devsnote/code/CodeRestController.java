package kr.devsnote.code;

import kr.devsnote.util.CommonUtil;
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
    public ResponseEntity<List<HashMap<String, Object>>> getCodeList(CodeVO vo) {
        log.info("[GET] /codes");
        List list = new ArrayList<HashMap<String, Object>>();
        list = service.getCodeList(vo);

        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    };


    @PostMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> insertCode(@RequestBody CodeVO model) {
        log.info("[POST] /codes");

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getCd()) || CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.insertCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;

    };

    @PutMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> updateCode(@RequestBody CodeVO model) {
        log.info("[PUT] /codes");

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getCd()) || CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.updateCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @DeleteMapping("/codes")
    public ResponseEntity<HashMap<String, Object>> deleteCode(@RequestBody CodeVO model) {
        log.info("[DELETE] /codes");

        String cd = model.getCd();
        log.info(cd);

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getCd()) || CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.deleteCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @GetMapping("/group-codes")
    public ResponseEntity<List<HashMap<String, Object>>> getGroupCodeList(CodeVO vo) {
        log.info("[GET] /group-codes");
        List list = new ArrayList<HashMap<String, Object>>();
        list = service.getGroupCodeList(vo);

        ResponseEntity<List<HashMap<String, Object>>> ret = new ResponseEntity<>(list, HttpStatus.OK);
        return ret;
    };


    @PostMapping("/group-codes")
    public ResponseEntity<HashMap<String, Object>> insertGroupCode(@RequestBody CodeVO model) {
        log.info("[POST] /group-codes");

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.insertGroupCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }

        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;

    };

    @PutMapping("/group-codes")
    public ResponseEntity<HashMap<String, Object>> updateGroupCode(@RequestBody CodeVO model) {
        log.info("[PUT] /group-codes");

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.updateGroupCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

    @DeleteMapping("/group-codes")
    public ResponseEntity<HashMap<String, Object>> deleteGroupCode(@RequestBody CodeVO model) {
        log.info("[DELETE] /group-codes");

        String cd = model.getCd();
        log.info(cd);

        HashMap map = new HashMap<String, Object>();
        if (CommonUtil.isEmpty(model.getGrp_cd())){
            map.put("result", "FAIL");
            map.put("msg", "필수값이 없습니다.");
        } else if (service.deleteGroupCode(model) > 0) {
            map.put("result", "OK");
        } else {
            map.put("result", "FAIL");
        }
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(map, HttpStatus.OK);
        return ret;
    };

}
