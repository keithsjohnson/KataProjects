package uk.co.keithsjohnson.jasperreports.model;

import java.io.Serializable;

public class City implements Serializable {

	private static final long serialVersionUID = 3977347517121289291L;

	private String name;

	private Long population;

	private Country country;

	public String getName() {
		return name;
	}

	public City setName(String name) {
		this.name = name;
		return this;
	}

	public Long getPopulation() {
		return population;
	}

	public City setPopulation(Long population) {
		this.population = population;
		return this;
	}

	public Country getCountry() {
		return country;
	}

	public City setCountry(Country country) {
		this.country = country;
		return this;
	}

}