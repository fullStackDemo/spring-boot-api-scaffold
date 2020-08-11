package com.scaffold.test.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.base.Result;
import com.scaffold.test.base.ResultCode;
import com.scaffold.test.base.ResultGenerator;
import com.scaffold.test.config.annotation.PassToken;
import com.scaffold.test.entity.User;
import com.scaffold.test.service.UserService;
import com.scaffold.test.utils.BaseUtils;
import com.scaffold.test.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器
 *
 * @author alex
 */


@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Autowired
    public RedissonClient redissonClient;

    /**
     * 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
     */
    private boolean kickoutAfter = false;

    /**
     * 同一个帐号最大会话数 默认1
     */
    private int maxSession = 1;

    public static final String PREFIX = "uni_token_";

    public static final String PREFIX_LOCK = "uni_token_lock_";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // token
        String token = BaseUtils.getToken();

        // 如果不是响应方法，静态资源直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 带 @PassToken 注解放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken annotation = method.getAnnotation(PassToken.class);
            if (annotation.required()) {
                return true;
            }
        }

        // 设置响应格式
        response.setContentType("application/json;charset=UTF-8");

        // 验证token非空
        if (token == null || token.equals("null")) {
            Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "无token,请重新登录");
            response.getWriter().write(getJSONObject(result));
            return false;
        }

        // 验证 redis中是否存在
        RBucket<User> bucket = redissonClient.getBucket(token);
        User rUser = bucket.get();
        if (rUser == null) {
            Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "无效token,请重新登录");
            response.getWriter().write(getJSONObject(result));
            return false;
        }

        // 验证TOKEN有效
        String currentUserId = BaseUtils.getCurrentUserId();
        if (currentUserId == null || currentUserId.equals("null")) {
            Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "访问异常，token不正确,请重新登录");
            response.getWriter().write(getJSONObject(result));
            return false;
        }

        // 验证用户是否存在
        User userQuery = new User();
        userQuery.setUserId(currentUserId);
        User user = userService.findUser(userQuery);
        if (user == null) {
            Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "用户不存在，token不正确,请重新登录");
            response.getWriter().write(getJSONObject(result));
            return false;
        }

        //jwt再次校验
        Boolean verify = JWTUtils.verify(token, user);
        if (!verify) {
            Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "非法访问,请重新登录");
            response.getWriter().write(getJSONObject(result));
            return false;
        }

        // 验证是否用户多地点登录
        return kickOut(token, response);
    }


    /**
     * 用户单登录
     *
     * @param token 令牌
     */
    public Boolean kickOut(String token, HttpServletResponse response) {
        User currentUser = BaseUtils.getCurrentUser();
        String username = currentUser.getUserName();
        String userKey = PREFIX + "deque_" + username;
        String lockKey = PREFIX_LOCK + username;
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(2, TimeUnit.SECONDS);

        try {
            RDeque<String> deque = redissonClient.getDeque(userKey);
            // 如果队列里没有此token，且用户没有被踢出；放入队列
            if (!deque.contains(token) && !currentUser.getKickout()) {
                deque.push(token);
            }

            // 如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > maxSession) {
                String kickoutSessionId;
                if (kickoutAfter) {
                    // 踢出前者
                    kickoutSessionId = deque.removeFirst();
                } else {
                    // 踢出后者
                    kickoutSessionId = deque.removeLast();
                }


                try {
                    RBucket<User> bucket = redissonClient.getBucket(kickoutSessionId);
                    User kickOutUser = bucket.get();

                    if (kickOutUser != null) {
                        kickOutUser.setKickout(true);
                        bucket.set(kickOutUser);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 如果被踢出了，提示退出
                if (currentUser.getKickout()) {
                    try {
                        Result result = ResultGenerator.setFailResult(ResultCode.UNAUTHORIZED, "您的账号已在其他设备登录");
                        response.getWriter().write(getJSONObject(result));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }

            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info(currentUser.getUserName() + " unlock");

            } else {
                log.info(currentUser.getUserName() + " already automatically release lock");
            }
        }

        return true;

    }


    // 响应结果转化格式
    private static String getJSONObject(Result result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", result.getCode());
        jsonObject.put("message", result.getMessage());
        return jsonObject.toJSONString();
    }
}
