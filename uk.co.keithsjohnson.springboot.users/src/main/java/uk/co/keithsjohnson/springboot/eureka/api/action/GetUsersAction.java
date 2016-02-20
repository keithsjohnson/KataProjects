package uk.co.keithsjohnson.springboot.eureka.api.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.keithsjohnson.springboot.eureka.api.model.User;
import uk.co.keithsjohnson.springboot.eureka.service.api.UsersProvider;

@RestController
public class GetUsersAction {
	private final DiscoveryClient discoveryClient;
	private final UsersProvider usersProvider;

	@Autowired
	public GetUsersAction(DiscoveryClient discoveryClient, UsersProvider usersProvider) {
		this.discoveryClient = discoveryClient;
		this.usersProvider = usersProvider;
	}

	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}

	@RequestMapping("users")
	public List<User> users() {
		return usersProvider.findAll();
	}
}
