package uk.co.keithsjohnson.springboot.eureka.service.api;

import uk.co.keithsjohnson.springboot.eureka.api.model.UserAccounts;

public interface UserAccountsProvider {

	UserAccounts findUsersAcountsForUser(String user);

}
