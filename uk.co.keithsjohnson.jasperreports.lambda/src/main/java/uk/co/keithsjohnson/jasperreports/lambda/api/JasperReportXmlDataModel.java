package uk.co.keithsjohnson.jasperreports.lambda.api;

import java.util.UUID;

public class JasperReportXmlDataModel {

	private String xmlSelectExpression;

	private String jrxmlLocation;

	private String jrxmlName;

	private String pdfLocation;

	private String pdfName;

	private String pdfNameWithUUID;

	public JasperReportXmlDataModel(String xmlSelectExpression, String jrxmlLocation, String jrxmlName,
			String pdfLocation, String pdfName) {
		super();
		this.xmlSelectExpression = xmlSelectExpression;
		this.jrxmlLocation = jrxmlLocation;
		this.jrxmlName = jrxmlName;
		this.pdfLocation = pdfLocation;
		this.pdfName = pdfName;
		this.pdfNameWithUUID = pdfName.substring(0, pdfName.length() - 4) + "-" + UUID.randomUUID().toString() + ".pdf";
	}

	public String getXmlSelectExpression() {
		return xmlSelectExpression;
	}

	public String getJrxmlLocation() {
		return jrxmlLocation;
	}

	public String getJrxmlName() {
		return jrxmlName;
	}

	public String getPdfLocation() {
		return pdfLocation;
	}

	public String getPdfName() {
		return pdfName;
	}

	public String getPdfNameWithUUID() {
		return pdfNameWithUUID;
	}

	@Override
	public String toString() {
		return "JasperReportXmlDataModel [xmlSelectExpression=" + xmlSelectExpression + ", jrxmlLocation="
				+ jrxmlLocation + ", jrxmlName=" + jrxmlName + ", pdfLocation=" + pdfLocation + ", pdfName=" + pdfName
				+ ", pdfNameWithUUID=" + pdfNameWithUUID + "]";
	}

}
