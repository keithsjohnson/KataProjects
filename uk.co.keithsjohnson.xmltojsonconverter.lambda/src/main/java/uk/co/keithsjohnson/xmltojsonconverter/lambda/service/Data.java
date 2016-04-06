package uk.co.keithsjohnson.xmltojsonconverter.lambda.service;

import java.util.LinkedHashMap;
import java.util.List;

public class Data {
	private List<LinkedHashMap<String, String>> data;

	public Data() {
	}

	public Data(List<LinkedHashMap<String, String>> data) {
		this.data = data;
	}

	public List<LinkedHashMap<String, String>> getData() {
		return data;
	}

	public void setData(List<LinkedHashMap<String, String>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Data [data=" + data + "]";
	}
}
