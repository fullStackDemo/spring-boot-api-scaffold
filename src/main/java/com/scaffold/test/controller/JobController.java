package com.scaffold.test.controller;


import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultCode;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.entity.Job;
import com.scaffold.test.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    // 添加
    @PostMapping("/add")
    public String add() {
        Job job = new Job();
        job.setAge(20);
        job.setPosition("总经理");
        job.setName("汪汪");
        return jobService.addJob(job);
    }

    // 查询所有
    @GetMapping("/list")
    public Result getList() {
        Result result;
        try {
            List<Job> jobList = jobService.findAll();
            result = ResultGenerator.getSuccessResult(jobList);
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultGenerator.getFailResult(e.getMessage());
        }
        return result;
    }

    // 更新
    @PostMapping("/update")
    public String update() {
        Job job = new Job();
        job.setAge(20);
        // 汪汪被降职
        job.setPosition("总经理助理");
        job.setName("汪汪");
        return jobService.updateJob(job);
    }

    // 删除
    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        // 汪汪被删除
        return jobService.deleteJobById(id);
    }


}

