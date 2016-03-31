package uk.co.keithsjohnson.jasperreports.lambda.api;

public class JasperReportJSONRequest {
	private String xmlRequest;

	public JasperReportJSONRequest() {
	}

	public JasperReportJSONRequest(String xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public String getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(String xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	@Override
	public String toString() {
		return "JasperReportJSONRequest [xmlRequest=" + xmlRequest + "]";
	}
}
