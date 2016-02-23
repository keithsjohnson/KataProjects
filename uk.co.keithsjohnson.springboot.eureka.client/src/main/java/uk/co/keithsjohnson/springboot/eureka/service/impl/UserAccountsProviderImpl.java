package uk.co.keithsjohnson.springboot.eureka.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;
import uk.co.keithsjohnson.springboot.eureka.api.model.User;
import uk.co.keithsjohnson.springboot.eureka.api.model.UserAccounts;
import uk.co.keithsjohnson.springboot.eureka.service.api.UserAccountsProvider;

@Component
@EnableCircuitBreaker
public class UserAccountsProviderImpl implements UserAccountsProvider {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public UserAccounts findUsersAcountsForUser(String user) {
		UserAccounts userAccounts = new UserAccounts();

		String userId = "TestUserId";

		userAccounts.setFullName(findFullNameForUser(userId));

		userAccounts.setAccounts(findAccountsForUser(userId));
		System.out.println(userAccounts);
		return userAccounts;
	}

	@HystrixCommand(fallbackMethod = "defaultAccounts", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") }, threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "30"),
					@HystrixProperty(name = "maxQueueSize", value = "101"),
					@HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
					@HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
			})
	public List<Account> findAccountsForUser(String userId) {
		ParameterizedTypeReference<List<Account>> accountsResponseType = new ParameterizedTypeReference<List<Account>>() {
		};

		ResponseEntity<List<Account>> accountsExchange = restTemplate.exchange(
				"http://accounts-service/accounts", HttpMethod.GET, null, accountsResponseType);

		List<Account> accounts = accountsExchange.getBody().stream().collect(Collectors.toList());
		return accounts;
	}

	public List<Account> defaultAccounts(String userId) {
		Account defaultAccount = new Account();
		defaultAccount.setName("Default Account");
		defaultAccount.setType("Default");

		List<Account> accounts = new ArrayList<>(1);
		accounts.add(defaultAccount);
		return accounts;
	}

	@HystrixCommand(fallbackMethod = "defaultFullNameForUser", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") }, threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "30"),
					@HystrixProperty(name = "maxQueueSize", value = "101"),
					@HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
					@HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
			})
	public String findFullNameForUser(String userId) {
		ParameterizedTypeReference<List<User>> usersResponseType = new ParameterizedTypeReference<List<User>>() {
		};

		ResponseEntity<List<User>> usersExchange = restTemplate.exchange(
				"http://users-service/users", HttpMethod.GET, null, usersResponseType);

		User user = usersExchange.getBody().get(0);
		return user.getFirstName() + " " + user.getLastName();
	}

	public String defaultFullNameForUser(String userId) {
		return "Default " + userId;
	}
}
