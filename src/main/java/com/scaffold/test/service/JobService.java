package com.scaffold.test.service;

import com.scaffold.test.entity.Job;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
public interface JobService extends IService<Job> {

    String addJob(Job job);

    List<Job> findAll();

    String updateJob(Job job);

    String deleteJobById(int id);

}
