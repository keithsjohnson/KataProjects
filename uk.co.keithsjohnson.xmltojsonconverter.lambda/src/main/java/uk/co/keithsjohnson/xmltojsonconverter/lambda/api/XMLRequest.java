package uk.co.keithsjohnson.xmltojsonconverter.lambda.api;

public class XMLRequest {
	private String xmlRequest;

	public XMLRequest() {
	}

	public XMLRequest(String xmlRequest) {
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
		return "XMLRequest [xmlRequest=" + xmlRequest + "]";
	}
}
