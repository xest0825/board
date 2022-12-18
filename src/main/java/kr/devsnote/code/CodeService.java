package kr.devsnote.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeDAO dao;

    public List<HashMap<String, Object>> getCodeList(CodeVO vo){
        return dao.getCodeList(vo);
    }

    public int insertCode(CodeVO vo) {
        return dao.insertCode(vo);
    }

    public int updateCode(CodeVO vo) {
        return dao.updateCode(vo);
    }

    public int deleteCode(CodeVO vo) {
        return dao.deleteCode(vo);
    }

    public List<HashMap<String, Object>> getGroupCodeList(CodeVO vo){
        return dao.getGroupCodeList(vo);
    }

    public int insertGroupCode(CodeVO vo) {
        return dao.insertGroupCode(vo);
    }

    public int updateGroupCode(CodeVO vo) {
        return dao.updateGroupCode(vo);
    }

    public int deleteGroupCode(CodeVO vo) {
        return dao.deleteGroupCode(vo);
    }
}
