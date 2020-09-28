package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.Student;
import com.scaffold.test.mapper.StudentMapper;
import com.scaffold.test.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author alex wong
 */

@Slf4j
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Override
    @Cacheable(value = "cacheData")
    public List<Student> findAll(){
        return studentMapper.selectAll();
    }

    /**
     * 缓存查询数据
     * @Cacheable 缓存数据到缓存 student 中
     * 其中缓存名称为 student 数据的 key 是 student 的 id
     * @param student s
     * @return
     */
    @Override
    @Cacheable(value = "cacheData", key = "#student.id")
    public Student findStudent(Student student) {
        log.warn("增加了student为{}的数据缓存", student);
        int id = student.getId();
        if(id == 0){
            return null;
        }
        return studentMapper.findStudent(student);
    }

    /**
     * 删除缓存
     * @CacheEvict 从缓存 student 中删除
     * 其中缓存名称为 student 数据的 key 是 student 的 id
     * @param student s
     */
    @Override
    @CacheEvict(value = "cacheData", key = "#student.id")
    public void deleteStudent(Student student) {
        log.warn("删除了student为{}的数据缓存", student);
    }

    /**
     * @CachePut 缓存新增的或更新的数据到缓存
     * 其中缓存名称为 student 数据的 key 是 student 的 id
     * @param student
     */
    @Override
    @CachePut(value = "cacheData", key = "#student.id")
    public void saveStudent(Student student) {
        log.warn("保存了id、key 为{}的数据缓存", student);
        studentMapper.insertStudent(student);
    }


    @Override
    @Cacheable(value = "cacheData", key = "#text")
    public Student testStudent(String text) {
        System.out.println("test" + text);
        Student student = new Student();
        student.setName(text);
        return student;
    }
}
