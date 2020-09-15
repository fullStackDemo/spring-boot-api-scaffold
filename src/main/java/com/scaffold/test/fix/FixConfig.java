package com.scaffold.test.fix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fix 配置
 */

@Slf4j
@Configuration
public class FixConfig {

    private final Map<InetSocketAddress, List<DynamicAcceptorSessionProvider.TemplateMapping>> dynamicSessionMappings = new HashMap<>();

    @Resource
    private FixServer fixServer;

    @Bean
    public SocketAcceptor socketAcceptor() throws ConfigError, FieldConvertError {
        SessionSettings settings = new SessionSettings("fix/acceptor.cfg");
        // 消息存储
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        // log
        SLF4JLogFactory logFactory = new SLF4JLogFactory(settings);
        // 消息
        DefaultMessageFactory messageFactory = new DefaultMessageFactory();
        // socket接收
        SocketAcceptor acceptor = new SocketAcceptor(fixServer, messageStoreFactory, settings, logFactory, messageFactory);
        return acceptor;
    }


}
