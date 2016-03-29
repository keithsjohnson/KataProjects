package uk.co.keithsjohnson.jasperreports.lambda.api;

public interface JasperReportIOProcessor {
	JasperReportRequestModel getJasperReportRequestModel(String xmlLocation, String xmlName);

	void writePdf(JasperReportRequestModel jasperReportRequestModel, byte[] pdfBytes);

	void deleteXmlFile(String xmlLocation, String xmlName);
}
