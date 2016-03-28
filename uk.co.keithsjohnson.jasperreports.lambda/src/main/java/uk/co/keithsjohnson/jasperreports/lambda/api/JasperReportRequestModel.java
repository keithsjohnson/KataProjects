package uk.co.keithsjohnson.jasperreports.lambda.api;

public class JasperReportRequestModel {

	private String xmlLocation;

	private String xmlName;

	private String xmlContents;

	private String xmlSelectExpression;

	private String jrxmlLocation;

	private String jrxmlName;

	private String jrxmlContents;

	private String pdfLocation;

	private String pdfName;

	public JasperReportRequestModel(String xmlLocation, String xmlName, String xmlContents, String xmlSelectExpression,
			String jrxmlLocation, String jrxmlName, String jrxmlContents, String pdfLocation, String pdfName) {
		super();
		this.xmlLocation = xmlLocation;
		this.xmlName = xmlName;
		this.xmlContents = xmlContents;
		this.xmlSelectExpression = xmlSelectExpression;
		this.jrxmlLocation = jrxmlLocation;
		this.jrxmlName = jrxmlName;
		this.jrxmlContents = jrxmlContents;
		this.pdfLocation = pdfLocation;
		this.pdfName = pdfName;
	}

	public String getXmlLocation() {
		return xmlLocation;
	}

	public String getXmlName() {
		return xmlName;
	}

	public String getXmlSelectExpression() {
		return xmlSelectExpression;
	}

	public String getXmlContents() {
		return xmlContents;
	}

	public String getJrxmlLocation() {
		return jrxmlLocation;
	}

	public String getJrxmlName() {
		return jrxmlName;
	}

	public String getJrxmlContents() {
		return jrxmlContents;
	}

	public String getPdfLocation() {
		return pdfLocation;
	}

	public String getPdfName() {
		return pdfName;
	}

	@Override
	public String toString() {
		return "JasperReportRequestModel [xmlLocation=" + xmlLocation + ", xmlName=" + xmlName + ", xmlContents="
				+ xmlContents + ", xmlSelectExpression=" + xmlSelectExpression + ", jrxmlLocation=" + jrxmlLocation
				+ ", jrxmlName=" + jrxmlName + ", jrxmlContents=" + jrxmlContents + ", pdfLocation=" + pdfLocation
				+ ", pdfName=" + pdfName + "]";
	}

}
