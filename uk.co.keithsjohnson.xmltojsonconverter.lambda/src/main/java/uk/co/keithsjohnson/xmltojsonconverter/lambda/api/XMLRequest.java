package uk.co.keithsjohnson.xmltojsonconverter.lambda.api;

public class XMLRequest {
	private String message;

	private String xmlRequest;

	public XMLRequest() {
	}

	public XMLRequest(String message, String xmlRequest) {
		this.xmlRequest = xmlRequest;
		this.message = message;
	}

	public String getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(String xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "XMLRequest [message=" + message + ", xmlRequest=" + xmlRequest + "]";
	}
}
