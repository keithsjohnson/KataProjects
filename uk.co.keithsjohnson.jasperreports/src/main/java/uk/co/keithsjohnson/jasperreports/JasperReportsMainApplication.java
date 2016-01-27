package uk.co.keithsjohnson.jasperreports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JasperReportsMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasperReportsMainApplication.class, args);
		System.out.println("Hi from JasperReportsMainApplication");
	}
}
