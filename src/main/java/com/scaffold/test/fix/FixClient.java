package com.scaffold.test.fix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.OrderID;

/**
 * Fix 客户端
 */

@Slf4j
@Component
public class FixClient implements Application {

    /**
     * 每当一个新的会话被创建，该方法被调用
     * @param sessionID
     */
    @Override
    public void onCreate(SessionID sessionID) {
        log.info("onCreate");
    }

    /**
     * 当登录操作成功完成时，该方法被调用
     * @param sessionID
     */
    @Override
    public void onLogon(SessionID sessionID) {
        log.error("onLogon登录");
    }

    /**
     * 当会话断开时触发，包括对方主动请求logout或者网络连接断开都会引发该事件
     * @param sessionID
     */
    @Override
    public void onLogout(SessionID sessionID) {
        log.error("onLogout退出");
    }

    /**
     * 所有发出的管理级别消息在发送出去之前，都会调用该方法
     * @param message
     * @param sessionID
     */
    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        log.error("发送会话消息");
        log.info("toAdmin:" + message);
    }

    /**
     * 每个管理级别的消息将通过该方法处理，如心跳，登录以及注销
     * @param message
     * @param sessionID
     * @throws FieldNotFound
     * @throws IncorrectDataFormat
     * @throws IncorrectTagValue
     * @throws RejectLogon
     */
    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.error("接收会话消息");
        log.info("fromAdmin:" + message);
    }

    /**
     * 所有应用级别的消息在发送出去之前，都会调用该方法。
     * 如果需要在每个发出的消息当中添加一个标签（Tag），在该方法是实现该需求最好的位置。
     * @param message
     * @param sessionID
     * @throws DoNotSend
     */
    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        log.error("发送业务消息");
        log.info("toApp:" + message);
    }

    /**
     * 每个应用级别的消息将通过该方法处理，如委托指令，执行报告，证券信息以及市场数据
     * @param message
     * @param sessionID
     * @throws FieldNotFound
     * @throws IncorrectDataFormat
     * @throws IncorrectTagValue
     * @throws UnsupportedMessageType
     */
    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.error("接收业务消息");
        log.info("fromApp:" + message);
        OrderID orderID = new OrderID();
        System.out.println("订单");
        System.out.println(message.getField(orderID));
    }

    public static void main(String[] args) throws ConfigError {
        // 测试
//        SessionSettings settings = new SessionSettings("fix/initiator.cfg");
//        FixClient application = new FixClient();
//        // 消息存储
//        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
//        // log
//        SLF4JLogFactory logFactory = new SLF4JLogFactory(settings);
//        // 消息
//        DefaultMessageFactory messageFactory = new DefaultMessageFactory();
//        // socket接收
//        SocketInitiator socketInitiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
//        socketInitiator.start();
    }
}
