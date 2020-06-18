package com.scaffold.test.task;

import com.scaffold.test.entity.Job;
import com.scaffold.test.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JobTask {

    @Autowired
    private JobService jobService;

    // 打印
    private static final Logger log = LoggerFactory.getLogger(JobTask.class);
    // 时间格式化
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // 每天 15：22 15:26  15:30 执行一次
    @Async
//    @Scheduled(cron = "0 22,26,30 15 * * ?")
    public void jobTask() {
        Job job = new Job();
        job.setName("test2");
        job.setAge(36);
        job.setPosition("助理");
        jobService.addJob(job);
        log.info("任务执行时间: " + dateFormat.format(new Date()));
    }
}
