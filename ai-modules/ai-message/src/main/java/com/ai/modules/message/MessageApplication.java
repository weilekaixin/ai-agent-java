package com.ai.modules.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 消息队列中心服务启动类
 *
 * @author root 2026-05-16 16:04
 */
@SpringBootApplication
public class MessageApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MessageApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  消息中心启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
