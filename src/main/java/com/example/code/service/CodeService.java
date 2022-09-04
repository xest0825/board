package com.example.code.service;

import com.example.code.dao.CodeDAO;
import com.example.code.vo.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeDAO dao;

    public List<HashMap<String, Object>> getCodeList(Code vo){
        return dao.getCodeList(vo);
    }

    public int insertCode(Code vo) {
        return dao.insertCode(vo);
    }

    public int updateCode(Code vo) {
        return dao.updateCode(vo);
    }

    public int deleteCode(Code vo) {
        return dao.deleteCode(vo);
    }

    public List<HashMap<String, Object>> getGroupCodeList(Code vo){
        return dao.getGroupCodeList(vo);
    }

    public int insertGroupCode(Code vo) {
        return dao.insertGroupCode(vo);
    }

    public int updateGroupCode(Code vo) {
        return dao.updateGroupCode(vo);
    }

    public int deleteGroupCode(Code vo) {
        return dao.deleteGroupCode(vo);
    }
}
