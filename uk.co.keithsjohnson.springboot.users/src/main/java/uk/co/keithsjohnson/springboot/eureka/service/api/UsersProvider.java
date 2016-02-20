package uk.co.keithsjohnson.springboot.eureka.service.api;

import java.util.List;

import uk.co.keithsjohnson.springboot.eureka.api.model.User;

public interface UsersProvider {

	List<User> findAll();

}
