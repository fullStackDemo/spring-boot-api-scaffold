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

    /**
     * 注册用户
     * @param user 用户
     * @return
     */
    int insertUser(User user);

    /**
     * 查找用户
     * @param user 用户
     * @return
     */
    User findUser(User user);

    /**
     * 验证验证码
     * @param code 验证码
     * @return
     */
    boolean checkCode(String code);

    /**
     * 退出登录
     * @param token
     */
    void logout(String token);

}
