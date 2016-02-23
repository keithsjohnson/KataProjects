package uk.co.keithsjohnson.springboot.eureka.api.model;

import java.util.List;

public class UserAccounts {
	private String fullName;

	private List<Account> accounts;

	public UserAccounts() {
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public String toString() {
		return "UserAccounts [fullName=" + fullName + ", accounts=" + accounts + "]";
	}

}
