package com.scaffold.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scaffold.test.entity.User;

/**
 * @author alex
 */

public interface UserMapper extends BaseMapper<User> {

    /**
     * 添加用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 查找用户
     * @param user
     * @return
     */
    User findUser(User user);
}
