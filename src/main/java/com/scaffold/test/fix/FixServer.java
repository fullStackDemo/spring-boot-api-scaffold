//package com.scaffold.test.fix;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import quickfix.*;
//
//import javax.annotation.Resource;
//
///**
// * fix 服务端
// */
//
//@Slf4j
//@Component
//public class FixServer extends MessageCracker implements Application {
//
//    @Resource
//    private FixMessageCracker messageCracker;
//
//    @Override
//    public void onCreate(SessionID sessionID) {
//        System.out.println("服务器启动时候调用此方法创建");
//    }
//
//    @Override
//    public void onLogon(SessionID sessionID) {
//        System.out.println("客户端登陆成功时候调用此方法");
//    }
//
//    @Override
//    public void onLogout(SessionID sessionID) {
//        System.out.println("客户端断开连接时候调用此方法");
//    }
//
//    @Override
//    public void toAdmin(Message message, SessionID sessionID) {
//        System.out.println("发送会话消息时候调用此方法");
//    }
//
//    @Override
//    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {System.out.println("接收会话类型消息时调用此方法");
//        try {
//            messageCracker.crack(message, sessionID);
//        } catch (UnsupportedMessageType unsupportedMessageType) {
//            log.error("接收会话类型: " + unsupportedMessageType.getMessage());
//            unsupportedMessageType.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
//        System.out.println("发送业务消息时候调用此方法");
//    }
//
//    @Override
//    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
//        System.out.println("接收业务消息时调用此方法");
//        messageCracker.crack(message, sessionID);
//    }
//
//}
