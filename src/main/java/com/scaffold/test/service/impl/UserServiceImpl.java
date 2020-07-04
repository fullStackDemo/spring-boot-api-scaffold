package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.User;
import com.scaffold.test.mapper.UserMapper;
import com.scaffold.test.service.UserService;
import com.scaffold.test.utils.HttpUtils;
import com.scaffold.test.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int insertUser(User user) {
        int flag;
        //判断用户是否存在
        User existUser = userMapper.findUser(user);
        if (existUser == null) {
            user.setUserId(UUIDUtils.getUUID());
            userMapper.insertUser(user);
            flag = 1;
        } else {
            flag = 0;
        }
        return flag;
    }

    @Override
    public User findUser(User user) {
        return userMapper.findUser(user);
    }

    @Override
    public boolean checkCode(String code) {
        HttpSession session = HttpUtils.getSession();
        String sessionKey =(String) session.getAttribute("RANDOMKEY");
        if(sessionKey == null){
            return false;
        }
        return sessionKey.equalsIgnoreCase(code);
    }

}
