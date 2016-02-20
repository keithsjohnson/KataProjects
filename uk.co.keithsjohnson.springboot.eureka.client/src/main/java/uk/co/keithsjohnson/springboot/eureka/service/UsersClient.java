package uk.co.keithsjohnson.springboot.eureka.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.co.keithsjohnson.springboot.eureka.api.model.User;

@FeignClient("users-service")
public interface UsersClient {

	@RequestMapping(method = RequestMethod.GET, value = "/users")
	List<User> users();
}
