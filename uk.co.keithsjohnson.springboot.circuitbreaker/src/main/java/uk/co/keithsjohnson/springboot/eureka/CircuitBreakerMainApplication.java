package uk.co.keithsjohnson.springboot.eureka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCircuitBreaker
@EnableFeignClients
@EnableEurekaClient
public class CircuitBreakerMainApplication {

	@Autowired
	private DiscoveryClient discoveryClient;

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = new SpringApplicationBuilder(
				CircuitBreakerMainApplication.class).web(true).run(args);
		System.out.println("Hi from CircuitBreakerMainApplication");

	}

	@RestController
	class ServiceInstanceRestController {
		private final DiscoveryClient discoveryClient;

		@Autowired
		public ServiceInstanceRestController(DiscoveryClient discoveryClient) {
			this.discoveryClient = discoveryClient;
		}

		@RequestMapping("/service-instances/{applicationName}")
		public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
			return this.discoveryClient.getInstances(applicationName);
		}
	}
}
