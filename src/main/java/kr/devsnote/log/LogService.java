package kr.devsnote.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogDAO dao;

    public int insertActionLog(HashMap<String, String> paramMap){
        // log.info("logService.insertActionLog " + paramMap.toString());
        dao.insertActionLog(paramMap);
        return 1;
    }

    /* ========== [오류 이력] ========== */
    public int insertErrorLog(HashMap<String, String> paramMap) {
        dao.insertErrorLog(paramMap);
        //log.info("logService.insertErrorLogb" + paramMap.toString());
        return 1;
    }


}
