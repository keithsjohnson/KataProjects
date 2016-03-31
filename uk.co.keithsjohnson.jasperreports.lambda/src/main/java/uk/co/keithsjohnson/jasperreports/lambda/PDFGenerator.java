package uk.co.keithsjohnson.jasperreports.lambda;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportIOProcessor;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;
import uk.co.keithsjohnson.jasperreports.lambda.service.FileJasperReportIOProcessorImpl;
import uk.co.keithsjohnson.jasperreports.lambda.service.JasperReportCreatorImpl;
import uk.co.keithsjohnson.jasperreports.lambda.service.JasperReportXmlDataProcessor;

public class PDFGenerator {

	private final JasperReportCreatorImpl jasperReportCreatorImpl = new JasperReportCreatorImpl();

	private final JasperReportIOProcessor jasperReportIOProcessor = new FileJasperReportIOProcessorImpl(
			new JasperReportXmlDataProcessor());

	public PDFGenerator() {
	}

	protected void generateReport(String xmlLocation, String xmlName) {
		JasperReportRequestModel jasperReportRequestModel = jasperReportIOProcessor
				.getJasperReportRequestModel(xmlLocation, xmlName);
		System.out.println(jasperReportRequestModel.toString());

		byte[] pdfBytes = jasperReportCreatorImpl.processJasperReportRequest(jasperReportRequestModel);

		jasperReportIOProcessor.writePdf(jasperReportRequestModel, pdfBytes);
	}
}