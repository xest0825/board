package kr.devsnote.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
public class ScheduleController {

    @Autowired
    Scheduler scheduler;

    @PutMapping("/schedule-cron")
    public ResponseEntity<HashMap<String, Object>> updateScheduleCron(HttpServletRequest req, @RequestBody ScheduleVO vo) throws InterruptedException {
        log.info("["+ req.getMethod() + "] /schedule");
        HashMap<String, Object> retMap = new HashMap<>();
        scheduler.stopScheduler();
        Thread.sleep(1000);
        scheduler.changeCronSet(vo.getSchedule());
        scheduler.startScheduler();
        retMap.put("result", "success");
        retMap.put("schedule", vo.getSchedule());
        retMap.put("msg", "cron changed");
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(retMap, HttpStatus.OK);
        return ret;
    };

    @PutMapping("/schedule-start")
    public ResponseEntity<HashMap<String, Object>> startScheduler(HttpServletRequest req, @RequestBody ScheduleVO vo) throws InterruptedException {
        log.info("["+ req.getMethod() + "] /schedule-start");
        HashMap<String, Object> retMap = new HashMap<>();
        scheduler.stopScheduler();
        scheduler.startScheduler();
        retMap.put("result", "success");
        retMap.put("msg", "scheduler started");
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(retMap, HttpStatus.OK);
        return ret;
    };

    @PutMapping("/schedule-pause")
    public ResponseEntity<HashMap<String, Object>> pauseScheduler(HttpServletRequest req, @RequestBody ScheduleVO vo) throws InterruptedException {
        log.info("["+ req.getMethod() + "] /schedule-pause");
        HashMap<String, Object> retMap = new HashMap<>();
        scheduler.stopScheduler();
        retMap.put("result", "success");
        retMap.put("msg", "scheduler stopped");
        ResponseEntity<HashMap<String, Object>> ret = new ResponseEntity<>(retMap, HttpStatus.OK);
        return ret;
    };



}
