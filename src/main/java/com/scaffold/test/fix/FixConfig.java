package com.scaffold.test.fix;

import lombok.extern.slf4j.Slf4j;
import org.quickfixj.CharsetSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;

import javax.annotation.Resource;

/**
 * Fix 配置
 */

@Slf4j
@Configuration
public class FixConfig {

    @Resource
    private FixServer fixServer;

    @Bean
    public SocketAcceptor socketAcceptor() throws Exception {
        CharsetSupport.setCharset("utf-8");
        SessionSettings settings = new SessionSettings("fix/acceptor.cfg");
        // 消息存储
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        // log
        SLF4JLogFactory logFactory = new SLF4JLogFactory(settings);
        // 消息
        DefaultMessageFactory messageFactory = new DefaultMessageFactory();
        // socket接收
        SocketAcceptor acceptor = new SocketAcceptor(fixServer, messageStoreFactory, settings, logFactory, messageFactory);
        acceptor.start();
        return acceptor;
    }


}
