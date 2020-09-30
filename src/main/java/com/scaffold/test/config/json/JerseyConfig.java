package com.scaffold.test.config.json;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {

        //response message writer
        register(JsonBodyWriter.class);

        //exceptions
//        register(ExceptionMapperSupport.class);

    }
}
