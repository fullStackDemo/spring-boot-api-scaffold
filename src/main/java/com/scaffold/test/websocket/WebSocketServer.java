package com.scaffold.test.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {

    // 存放当前连接的客户端的Websocket对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    // 与客户端的连接会话，通过它来给客户端发送数据
    private Session session;
    // 当前用户id
    private String userId;
    // 在线人数
    private static int onlineNumber = 0;


    /**
     * 连接打开时调用
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        // 判断连接是否已经成立
        if (webSocketMap.containsKey(userId)) {
            // 已存在，先移除再添加
            webSocketMap.remove(userId);
        } else {
            // 不存在，直接添加
            webSocketMap.put(userId, this);
            // 增加在线人数
            addOnlineNumber();

        }

        log.info("连接用户：" + userId + ",当前连接人数为：" + getOnlineNumber());

    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineNumber();
        }
        log.info("用户退出：" + userId + ",当前连接人数为：" + getOnlineNumber());
    }

    /**
     * 收到客户端信息时调用
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("用户消息:" + userId + ",报文:" + message);

        // 判断消息是否非空
        if (StringUtils.isNotBlank(message)) {
            //解析消息
            JSONObject messageInfo = JSON.parseObject(message);
            String userId = messageInfo.getString("userId");
            // 验证UserId, 如果存在，发送消息
            if (StringUtils.isNoneBlank(userId) && webSocketMap.containsKey(userId)) {
                webSocketMap.get(userId).sendMessage(messageInfo.toJSONString());

            } else {
                log.error("用户不存在");
            }
        }
    }

    /**
     * 连接出错时调用
     *
     * @param session
     * @param err
     */
    @OnError
    public void onError(Session session, Throwable err) {
        log.error(this.userId + "连接出错，" + err.getMessage());
        err.printStackTrace();
    }

    /**
     * 服务端向客户端发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息给客户端
     *
     * @param message
     * @param userId
     * @throws IOException
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息给" + userId + ",内容为：" + message);
        if (StringUtils.isNoneBlank(message) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + "不在线");
        }
    }

    // 获取在线人数，锁定线程
    public static synchronized int getOnlineNumber() {
        return onlineNumber;
    }

    // 增加人数
    public static synchronized void addOnlineNumber() {
        onlineNumber++;
    }

    // 减少人数
    public static synchronized void subOnlineNumber() {
        onlineNumber--;
    }


}
