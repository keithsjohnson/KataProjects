package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XMLToJSONConverter {

	public static void main(String[] args) throws Exception {
		XmlMapper xmlMapper = new XmlMapper();
		List entries = xmlMapper.readValue(new File("input.xml"), List.class);

		ObjectMapper jsonMapper = new ObjectMapper();
		String json = jsonMapper.writeValueAsString(entries);
		System.out.println(json);
		// [{"name":"AXL","time":"19-07","price":"11.34"},{"name":"AIK","time":"19-07","price":"13.54"},{"name":"ALO","time":"19-07","price":"16.32"},{"name":"APO","time":"19-07","price":"13.56"}]
	}
}
