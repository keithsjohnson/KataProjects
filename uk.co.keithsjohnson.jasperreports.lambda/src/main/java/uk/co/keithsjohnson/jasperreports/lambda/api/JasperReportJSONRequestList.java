package uk.co.keithsjohnson.jasperreports.lambda.api;

import java.util.List;

public class JasperReportJSONRequestList {
	private List<JasperReportJSONRequest> xmlRequestData;

	public JasperReportJSONRequestList() {
	}

	public JasperReportJSONRequestList(List<JasperReportJSONRequest> xmlRequestData) {
		this.xmlRequestData = xmlRequestData;
	}

	public List<JasperReportJSONRequest> getXmlRequestData() {
		return xmlRequestData;
	}

	public void setXmlRequestData(List<JasperReportJSONRequest> xmlRequestData) {
		this.xmlRequestData = xmlRequestData;
	}

	@Override
	public String toString() {
		return "JasperReportJSONRequestList [xmlRequestData=" + xmlRequestData + "]";
	}
}
