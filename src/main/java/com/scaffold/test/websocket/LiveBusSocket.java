package com.scaffold.test.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scaffold.test.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/liveBus")
public class LiveBusSocket {

    // 存放当前连接的客户端的Websocket对象
    private static ConcurrentHashMap<String, LiveBusSocket> webSocketMap = new ConcurrentHashMap<>();
    // 与客户端的连接会话，通过它来给客户端发送数据
    private Session session;
    // 当前用户id
    private String routeCode;
    // 当前会话连接id
    private String sessionId;


    /**
     * 连接打开时调用
     *
     * @param session 会话
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        JSONObject query = getSessionQuery(session);
        String routeCode = query.getString("routeCode");
        String sessionId = query.getString("sessionId");

        this.routeCode = routeCode;
        this.sessionId = sessionId;
        // 判断连接是否已经成立
        if (webSocketMap.containsKey(sessionId)) {
            // 已存在，先移除再添加
            webSocketMap.remove(sessionId);
        }
        webSocketMap.put(sessionId, this);
    }

    /**
     * 连接关闭时调用
     */
    @OnClose
    public void onClose(Session session) {
        JSONObject query = getSessionQuery(session);
        String sessionId = query.getString("sessionId");

        if (webSocketMap.containsKey(sessionId)) {
            webSocketMap.remove(sessionId);
        }
    }

    /**
     * 收到客户端信息时调用
     *
     * @param message 接收消息
     * @param session session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("用户消息:" + routeCode + ",报文:" + message);

        // 判断消息是否非空
        if (StringUtils.isNotBlank(message)) {
            //解析消息
            JSONObject messageInfo = JSON.parseObject(message);
            String sessionId = messageInfo.getString("sessionId");
//            // 用户查询内容
//            String query = messageInfo.getString("query");
//            // 验证routeCode, 如果存在，发送消息
//            if (StringUtils.isNoneBlank(sessionId) && webSocketMap.containsKey(sessionId)) {
//                String msg = null;
//                /*
//                 * 查询人数
//                 */
//                if (query.equals("onLineNumber")) {
//                    msg = generateMessage(200, "当前在线人数：" + getOnlineNumber());
//                }
//                webSocketMap.get(sessionId).sendMessage(msg);
//            } else {
//                log.error("用户不存在");
//            }
        }
    }

    /**
     * 连接出错时调用
     *
     * @param session session
     * @param err
     */
    @OnError
    public void onError(Session session, Throwable err) {
        JSONObject sessionQuery = getSessionQuery(session);
        log.error(sessionQuery.getString("sessionId") + "连接出错，" + err.getMessage());
        err.printStackTrace();
    }

    /**
     * 单发消息
     * 服务端向客户端发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 单发消息
     * 发送自定义消息给客户端
     *
     * @param message
     * @param sessionId
     * @throws IOException
     */
    public static void sendInfo(String message, String sessionId) throws IOException {
        if (StringUtils.isNoneBlank(message) && webSocketMap.containsKey(sessionId)) {
            webSocketMap.get(sessionId).sendMessage(message);
        } else {
            log.error("用户" + sessionId + "不在线");
        }
    }

    /**
     * 群发消息
     *
     * @param message 消息
     */
    public void sendMessageAll(String message) {
        // 遍历 HashMap
        for (String key : webSocketMap.keySet()) {
            // 排除当前连接用户
            try {
                webSocketMap.get(key).sendMessage(message);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取参数
     *
     * @param session session
     * @return JSONObject
     */
    public static JSONObject getSessionQuery(Session session) {
        String queryString = session.getQueryString();
        JSONObject query = SystemUtils.getQuery(queryString);
        return query;
    }


    /**
     * 生成消息
     *
     * @param code
     * @param message
     * @return
     */
    public String generateMessage(int code, String message) {
        Map<Object, Object> wsResult = new HashMap<>();
        wsResult.put("message", message);
        wsResult.put("code", code);
        return JSONObject.toJSONString(wsResult);
    }


}
