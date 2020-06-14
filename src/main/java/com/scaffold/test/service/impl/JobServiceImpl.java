package com.scaffold.test.service.impl;

import com.scaffold.test.entity.Job;
import com.scaffold.test.mapper.JobMapper;
import com.scaffold.test.service.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Autowired
    private JobMapper jobMapper;

    @Override
    public String addJob(Job job) {
        jobMapper.insertJob(job);
        return "添加成功";
    }

    @Override
    public List<Job> findAll() {
        return jobMapper.selectAll();
    }

    @Override
    public String updateJob(Job job) {
        jobMapper.updateJob(job);
        return "更新成功";
    }

    @Override
    public String deleteJobById(int id) {
        jobMapper.deleteJobById(id);
        return "删除成功";
    }
}
