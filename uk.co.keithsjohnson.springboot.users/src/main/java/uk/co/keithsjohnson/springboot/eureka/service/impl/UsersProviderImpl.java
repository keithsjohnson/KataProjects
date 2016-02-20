package uk.co.keithsjohnson.springboot.eureka.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import uk.co.keithsjohnson.springboot.eureka.api.model.User;
import uk.co.keithsjohnson.springboot.eureka.service.api.UsersProvider;

@Component
public class UsersProviderImpl implements UsersProvider {

	@Override
	public List<User> findAll() {

		User keith = new User();
		keith.setFirstName("Keith");
		keith.setLastName("Johnson");
		keith.setUsername("keithj");

		List<User> users = new ArrayList<>();

		users.add(keith);

		return users;
	}
}
