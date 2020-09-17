package com.scaffold.test;

import com.scaffold.test.fix.FixClient;
import org.quickfixj.CharsetSupport;
import quickfix.*;

import java.util.Scanner;

public class SenderAppTest {

    static {
        try {
            start("fix/initiator.cfg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(String config) {
        try {
            // 测试
            CharsetSupport.setCharset("utf-8");
            SessionSettings settings = new SessionSettings(config);
            FixClient application = new FixClient();
            // 消息存储
            MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
            // log
            SLF4JLogFactory logFactory = new SLF4JLogFactory(settings);
            // 消息
            DefaultMessageFactory messageFactory = new DefaultMessageFactory();
            // socket接收
            SocketInitiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
            initiator.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        if (scan.hasNext()) {
            System.exit(0);
        }
    }

}
