package uk.co.keithsjohnson.jasperreports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import uk.co.keithsjohnson.jasperreports.service.ApiService;

@SpringBootApplication
public class JasperReportsMainApplication {

	@Autowired
	private static ApiService apiService;

	public static void main(String[] args) {
		ConfigurableApplicationContext configurableApplicationContext = SpringApplication
				.run(JasperReportsMainApplication.class, args);
		System.out.println("Hi from JasperReportsMainApplication");

		apiService = configurableApplicationContext.getBean(ApiService.class);

		JasperReportsMainApplication jasperReportsMainApplication = new JasperReportsMainApplication();
		jasperReportsMainApplication.report();
	}

	private void report() {
		apiService.generateReport(apiService.getCities());
	}
}
