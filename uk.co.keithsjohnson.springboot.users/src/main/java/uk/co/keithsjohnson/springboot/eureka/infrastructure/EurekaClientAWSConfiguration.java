package uk.co.keithsjohnson.springboot.eureka.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.netflix.appinfo.AmazonInfo;

@Configuration
public class EurekaClientAWSConfiguration {
	@Bean
	@Profile("!default")
	public EurekaInstanceConfigBean eurekaInstanceConfig() {
		EurekaInstanceConfigBean b = new EurekaInstanceConfigBean();
		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
		b.setDataCenterInfo(info);
		return b;
	}

	@Autowired
	private DiscoveryClient discoveryClient;

	public String serviceUrl() {
		List<ServiceInstance> list = discoveryClient.getInstances("STORES");
		if (list != null && list.size() > 0) {
			return list.get(0).getUri().toString();
		}
		return null;
	}
}
