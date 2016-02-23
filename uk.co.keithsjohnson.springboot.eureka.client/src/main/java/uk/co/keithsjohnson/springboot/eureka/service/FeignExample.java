package uk.co.keithsjohnson.springboot.eureka.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;
import uk.co.keithsjohnson.springboot.eureka.api.model.User;

@Component
public class FeignExample implements CommandLineRunner {

	@Autowired
	private AccountsClient accountsClient;

	@Autowired
	private UsersClient usersClient;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hi from FeignExample");
		accounts().forEach(System.out::println);
		users().forEach(System.out::println);
	}

	public List<Account> accountsFallback() {
		Account fallbackAccount = new Account();
		fallbackAccount.setName("Fallback Account");
		fallbackAccount.setType("Fallback");

		List<Account> accounts = new ArrayList<Account>();
		return accounts;
	}

	@HystrixCommand(fallbackMethod = "accountsFallback")
	public List<Account> accounts() {
		return accountsClient.accounts();
	}

	public List<User> usersFallback() {
		User fallbackUser = new User();
		fallbackUser.setFirstName("Fall");
		fallbackUser.setLastName("Back");
		fallbackUser.setUsername("username");
		List<User> users = new ArrayList<User>();
		return users;
	}

	@HystrixCommand(fallbackMethod = "usersFallback")
	public List<User> users() {
		return usersClient.users();
	}

}
