package uk.co.keithsjohnson.jasperreports.lambda.api;

import java.util.Arrays;

public class JasperReportJSONRequest {
	private String[] xmlRequestData;

	public JasperReportJSONRequest() {
	}

	public JasperReportJSONRequest(String[] xmlRequestData) {
		this.xmlRequestData = xmlRequestData;
	}

	public String[] getXmlRequestData() {
		return xmlRequestData;
	}

	public void setXmlRequestData(String[] xmlRequestData) {
		this.xmlRequestData = xmlRequestData;
	}

	@Override
	public String toString() {
		return "JasperReportJSONRequest [xmlRequestData=" + Arrays.toString(xmlRequestData) + "]";
	}
}
