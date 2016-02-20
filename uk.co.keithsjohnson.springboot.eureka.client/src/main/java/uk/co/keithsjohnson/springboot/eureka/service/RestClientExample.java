package uk.co.keithsjohnson.springboot.eureka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;
import uk.co.keithsjohnson.springboot.eureka.api.model.User;

@Component
public class RestClientExample implements CommandLineRunner {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hi from RestClientExample");

		ParameterizedTypeReference<List<Account>> accountsResponseType = new ParameterizedTypeReference<List<Account>>() {
		};

		ResponseEntity<List<Account>> accountsExchange = restTemplate.exchange(
				"http://accounts-service/accounts", HttpMethod.GET, null, accountsResponseType);

		accountsExchange.getBody().forEach(System.out::println);

		ParameterizedTypeReference<List<User>> usersResponseType = new ParameterizedTypeReference<List<User>>() {
		};

		ResponseEntity<List<User>> usersExchange = restTemplate.exchange(
				"http://users-service/users", HttpMethod.GET, null, usersResponseType);

		usersExchange.getBody().forEach(System.out::println);

	}

}
