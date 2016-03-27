package uk.co.keithsjohnson.jasperreports.service;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import uk.co.keithsjohnson.jasperreports.model.City;
import uk.co.keithsjohnson.jasperreports.model.Country;
import uk.co.keithsjohnson.jasperreports.report.CustomJRDataSource;

@Component
public class ApiService {
	final static Logger logger = Logger.getLogger(ApiService.class);

	private Map<String, City> cities = new ConcurrentHashMap<>();

	public void addCity(String cityName, String countryName, Long population) {
		City city = new City().setName(cityName).setPopulation(population);
		Country country = new Country().setName(countryName);
		city.setCountry(country);
		country.addCity(city);
		cities.put(cityName, city);
	}

	public void generateReport(List<City> cities) {
		JasperReport jasperReport;
		JasperPrint jasperPrint;
		try {
			jasperReport = JasperCompileManager.compileReport("src/main/resources/static/report.jrxml");
			CustomJRDataSource<City> dataSource = new CustomJRDataSource<City>().initBy(cities);
			jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), dataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/resources/static/report.pdf");
		} catch (JRException e) {
			throw new RuntimeException(e);
		}

		try {
			jasperReport = JasperCompileManager.compileReport("src/main/resources/static/XMLDSReport.jrxml");
			JRXmlDataSource dataSource = new JRXmlDataSource(
					new FileInputStream("src/main/resources/static/simple.xml"), "/breakfast_menu/food");
			jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), dataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/resources/static/simplereport.pdf");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		try {
			jasperReport = JasperCompileManager.compileReport("src/main/resources/static/report.jrxml");
			JRXmlDataSource dataSource = new JRXmlDataSource(
					new FileInputStream("src/main/resources/static/report.xml"), "/cities/city");
			jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<String, Object>(), dataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/resources/static/xmlreport.pdf");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PostConstruct
	public void init() {
		logger.info("ApiService.init()");
		addCity("Los Angeles", "USA", 10000L);
		addCity("New York", "USA", 20000L);
		addCity("Washington", "USA", 30000L);
		addCity("Moscow", "RUSSIA", 90000L);
		addCity("Novosibirsk", "RUSSIA", 10000L);
	}

	public List<City> getCities() {
		List<City> cities2 = new ArrayList<>();
		cities2.addAll(cities.values());
		return cities2;
	}
}
