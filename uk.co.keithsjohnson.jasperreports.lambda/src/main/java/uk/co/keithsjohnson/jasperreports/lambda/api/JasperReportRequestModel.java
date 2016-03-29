package uk.co.keithsjohnson.jasperreports.lambda.api;

public class JasperReportRequestModel {

	private String xmlLocation;

	private String xmlName;

	private String xmlContents;

	private JasperReportXmlDataModel jasperReportXmlDataModel;

	private String jrxmlContents;

	public JasperReportRequestModel(String xmlLocation, String xmlName, String xmlContents,
			JasperReportXmlDataModel jasperReportXmlDataModel, String jrxmlContents) {
		super();
		this.xmlLocation = xmlLocation;
		this.xmlName = xmlName;
		this.xmlContents = xmlContents;
		this.jasperReportXmlDataModel = jasperReportXmlDataModel;
		this.jrxmlContents = jrxmlContents;
	}

	public String getXmlLocation() {
		return xmlLocation;
	}

	public String getXmlName() {
		return xmlName;
	}

	public String getXmlContents() {
		return xmlContents;
	}

	public JasperReportXmlDataModel getJasperReportXmlDataModel() {
		return jasperReportXmlDataModel;
	}

	public String getJrxmlContents() {
		return jrxmlContents;
	}

	@Override
	public String toString() {
		return "JasperReportRequestModel [xmlLocation=" + xmlLocation + ", xmlName=" + xmlName + ", xmlContents="
				+ xmlContents + ", jasperReportXmlDataModel=" + jasperReportXmlDataModel.toString() + ", jrxmlContents="
				+ jrxmlContents + "]";
	}

}
