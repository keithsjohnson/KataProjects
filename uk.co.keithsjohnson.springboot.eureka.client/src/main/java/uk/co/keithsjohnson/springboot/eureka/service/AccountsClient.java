package uk.co.keithsjohnson.springboot.eureka.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;

@FeignClient("accounts-service")
public interface AccountsClient {

	@RequestMapping(method = RequestMethod.GET, value = "/accounts")
	List<Account> accounts();
}
