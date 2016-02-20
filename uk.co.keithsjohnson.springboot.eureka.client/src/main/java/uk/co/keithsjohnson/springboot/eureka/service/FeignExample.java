package uk.co.keithsjohnson.springboot.eureka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FeignExample implements CommandLineRunner {

	@Autowired
	private AccountsClient accountsClient;

	@Autowired
	private UsersClient usersClient;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hi from FeignExample");
		accountsClient.accounts().forEach(System.out::println);
		usersClient.users().forEach(System.out::println);
	}

}
