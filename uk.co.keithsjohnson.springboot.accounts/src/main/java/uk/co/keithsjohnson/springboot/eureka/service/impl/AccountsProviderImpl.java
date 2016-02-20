package uk.co.keithsjohnson.springboot.eureka.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.keithsjohnson.springboot.eureka.api.model.Account;
import uk.co.keithsjohnson.springboot.eureka.service.api.AccountsProvider;

@Component
public class AccountsProviderImpl implements AccountsProvider {

	@Override
	public List<Account> findAll() {

		Account barclaysCurrentAccount = new Account();
		barclaysCurrentAccount.setName("Barclays");
		barclaysCurrentAccount.setType("Current");

		List<Account> accounts = new ArrayList<>();

		accounts.add(barclaysCurrentAccount);

		return accounts;
	}
}
