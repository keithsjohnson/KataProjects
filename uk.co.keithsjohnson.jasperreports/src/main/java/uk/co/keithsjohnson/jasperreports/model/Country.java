package uk.co.keithsjohnson.jasperreports.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Country implements Serializable {
	private static final long serialVersionUID = 7215298992738155751L;

	private String name;

	private List<City> cities = new ArrayList<City>();

	public String getName() {
		return name;
	}

	public Country setName(String name) {
		this.name = name;
		return this;
	}

	public List<City> getCities() {
		return cities;
	}

	public Country setCities(List<City> cities) {
		this.cities = cities;
		return this;
	}

	public Country addCity(City city) {
		if (!cities.contains(city)) {
			cities.add(city);
		}
		return this;
	}

}