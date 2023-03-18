package kr.devsnote.contract;

import kr.devsnote.insco.InscoDAO;
import kr.devsnote.insco.InscoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractDAO dao;

    public List<HashMap<String, Object>> getContractList(ContractVO vo){
        return dao.getContractList(vo);
    }


    public HashMap<String, Object> getContract(ContractVO vo){
        int ret = 0;
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        List<HashMap<String, Object>> list = dao.getContractList(vo);
        if (list.size() > 0) {
            retMap = list.get(0);
        }
        return retMap;
    }

    public int insertContract(ContractVO vo) {
        return dao.insertContract(vo);
    }

    public int updateContract(ContractVO vo) {
        return dao.updateContract(vo);
    }

    public int deleteContract(ContractVO vo) {
        return dao.deleteContract(vo);
    }
}
