package uk.co.keithsjohnson.springboot.eureka.api.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.keithsjohnson.springboot.eureka.api.model.UserAccounts;
import uk.co.keithsjohnson.springboot.eureka.service.api.UserAccountsProvider;

@RestController
public class GetUserAccounts {

	private UserAccountsProvider userAccountsProvider;

	@Autowired
	public GetUserAccounts(UserAccountsProvider userAccountsProvider) {
		this.userAccountsProvider = userAccountsProvider;
	}

	@RequestMapping("userAccounts")
	public UserAccounts userAccounts() {
		return userAccountsProvider.findUsersAcountsForUser("");
	}
}
