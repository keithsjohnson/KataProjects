package uk.co.keithsjohnson.springboot.eureka.service;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

@Component
public class DiscoveryClientExample implements CommandLineRunner {

	@Autowired
	DiscoveryClient discoveryclient;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hi from DiscoveryClientExample");

		discoveryclient.getInstances("accounts-service").forEach(s -> {
			System.out.println("" + ToStringBuilder.reflectionToString(s));
		});
		discoveryclient.getInstances("users-service").forEach(s -> {
			System.out.println("" + ToStringBuilder.reflectionToString(s));
		});
	}

}
