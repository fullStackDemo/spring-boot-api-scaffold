package com.scaffold.test.mapper;

import com.scaffold.test.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author alex wong
 * @since 2020-06-14
 */
public interface JobMapper extends BaseMapper<Job> {

    void insertJob(Job job);

    List<Job> selectAll();

    void updateJob(Job job);

    void deleteJobById(int id);

}
