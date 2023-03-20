package kr.devsnote.schedule;

import kr.devsnote.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageField;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

@Slf4j
@Component
public class Scheduler {

    private ThreadPoolTaskScheduler scheduler;
    private String cron = "0 0 0 * * *";

    @Scheduled(cron = "0 * * * * *")
    public void scheduleLog(){
        log.info("scheduled log " + CommonUtil.getCurrentDateTime());
    }

    public void startScheduler(){
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        // scheduler setting
        scheduler.schedule(getRunnable(), getTrigger());
    }
    public void changeCronSet(String cron) {
        this.cron = cron;
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }

    private Runnable getRunnable() {
        // do something
        return () -> {
            log.info("dynamic scheduled log " + String.valueOf(LocalDateTime.now()));
        };
    }

    private Trigger getTrigger() {
        // cronSetting
        return new CronTrigger(cron);
    }

    @PostConstruct
    public void init() {
        startScheduler();
    }

    @PreDestroy
    public void destroy() {
        stopScheduler();
    }


}
