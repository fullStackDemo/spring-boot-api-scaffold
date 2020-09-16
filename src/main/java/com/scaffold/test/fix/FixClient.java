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

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("onCreate");
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.error("onLogon");
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.error("onLogout");
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        log.error("发送会话消息");
        log.info("toAdmin:" + message);
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        log.error("接收会话消息");
        log.info("fromAdmin:" + message);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
        log.error("发送业务消息");
        log.info("toApp:" + message);
    }

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
