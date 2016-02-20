package uk.co.keithsjohnson.springboot.eureka.api.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;
import uk.co.keithsjohnson.springboot.eureka.service.api.AccountsProvider;

@RestController
public class GetAccountsAction {
	private final DiscoveryClient discoveryClient;
	private final AccountsProvider accountsProvider;

	@Autowired
	public GetAccountsAction(DiscoveryClient discoveryClient, AccountsProvider usersProvider) {
		this.discoveryClient = discoveryClient;
		this.accountsProvider = usersProvider;
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@RequestMapping("accounts")
	public List<Account> accounts() {
		return accountsProvider.findAll();
	}
}
