package uk.co.keithsjohnson.springboot.eureka.service.api;

import java.util.List;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;

public interface AccountsProvider {

	List<Account> findAll();

}
