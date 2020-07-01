package com.scaffold.test.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 读取基础配置信息
 * */
@Component
@Configuration
@Data
public class BaseApplication {

    // 导出路径
    @Value("${user.export}")
    private String exportPath;

    @Value("${user.mac-export}")
    private String macExportPath;

}
