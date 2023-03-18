package kr.devsnote.insco;

import kr.devsnote.insco.InscoDAO;
import kr.devsnote.insco.InscoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class InscoService {

    @Autowired
    private InscoDAO dao;

    public List<HashMap<String, Object>> getInscoList(InscoVO vo){
        return dao.getInscoList(vo);
    }


    public HashMap<String, Object> getInsco(InscoVO vo){
        int ret = 0;
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = dao.getInscoList(vo);
        if (list.size() > 0) {
            retMap = list.get(0);
        }
        return retMap;
    }

    public int insertInsco(InscoVO vo) {
        return dao.insertInsco(vo);
    }

    public int updateInsco(InscoVO vo) {
        return dao.updateInsco(vo);
    }

    public int deleteInsco(InscoVO vo) {
        return dao.deleteInsco(vo);
    }
}
