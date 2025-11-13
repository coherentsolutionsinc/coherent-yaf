package com.coherentsolutions.yaf.junit;

import com.coherentsolutions.yaf.core.config.YafConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.coherentsolutions.yaf")
@EnableAutoConfiguration
public class MyTestConfig extends YafConfig {

    /**
     * Instantiates a new Yaf config.
     *
     * @param args the args
     */
    public MyTestConfig(ApplicationArguments args) {
        super(args);
    }
}
