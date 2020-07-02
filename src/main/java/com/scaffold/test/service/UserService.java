package com.scaffold.test.service;

import com.scaffold.test.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-02
 */
public interface UserService extends IService<User> {

    int insertUser(User user);

    User findUser(User user);


}
