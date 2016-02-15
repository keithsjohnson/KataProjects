package uk.co.keithsjohnson.springboot.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerMainApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication
				.run(EurekaServerMainApplication.class, args);
		System.out.println("Hi from EurekaServerMainApplication");

	}

}
