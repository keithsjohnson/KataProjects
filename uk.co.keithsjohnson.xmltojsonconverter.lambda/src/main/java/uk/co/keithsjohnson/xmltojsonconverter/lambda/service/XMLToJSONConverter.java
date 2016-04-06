package uk.co.keithsjohnson.xmltojsonconverter.lambda.service;

import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XMLToJSONConverter {
	private final XmlMapper XML_MAPPER = new XmlMapper();

	private final static ObjectMapper JSON_MAPPER = new ObjectMapper();

	public String convertXMLToJSON(String xml) {
		try {
			List<LinkedHashMap<String, String>> entries = XML_MAPPER.readValue(xml, List.class);

			Data data = new Data(entries);
			return JSON_MAPPER.writeValueAsString(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
