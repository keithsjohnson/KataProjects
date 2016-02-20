package uk.co.keithsjohnson.springboot.eureka.infrastructure;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class StoreIntegration {

	@HystrixCommand(fallbackMethod = "defaultStores")
	public Object getStores(Map<String, Object> parameters) {
		return null;
	}

	public Object defaultStores(Map<String, Object> parameters) {
		return null;
	}
}