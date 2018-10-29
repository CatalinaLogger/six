package com.maybe;

import com.maybe.sys.common.socket.WebSocketServer;
import org.flowable.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan
@ComponentScan(basePackages = "org.flowable.app")
@EnableScheduling
@SpringBootApplication(
		exclude = {
				SecurityAutoConfiguration.class,
				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
				UserDetailsServiceAutoConfiguration.class
		}
)
public class Application {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
		//解决WebSocket不能注入的问题
		WebSocketServer.setApplicationContext(configurableApplicationContext);
	}

}
