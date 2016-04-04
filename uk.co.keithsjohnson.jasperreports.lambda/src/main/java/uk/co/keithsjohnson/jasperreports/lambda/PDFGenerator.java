package uk.co.keithsjohnson.jasperreports.lambda;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.bind.DatatypeConverter;

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

	protected void generateReport(String xmlStringAsBase64) {

		String xmlString = convertXMLBase64StringToXmlString(xmlStringAsBase64);

		System.out.println(xmlString);

		JasperReportRequestModel jasperReportRequestModel = jasperReportIOProcessor
				.getXmlContentsForString(xmlString);
		System.out.println(jasperReportRequestModel.toString());

		byte[] pdfBytes = jasperReportCreatorImpl.processJasperReportRequest(jasperReportRequestModel);

		jasperReportIOProcessor.writePdf(jasperReportRequestModel, pdfBytes);
	}

	public String convertXMLBase64StringToXmlString(String xmlStringAsBase64) {
		return new String(convertStringToByteArrayUsingBase64(xmlStringAsBase64));
	}

	public String convertXMLStringToXmlBase64String(String xmlString) {
		return convertByteArrayToStringUsingBase64(xmlString.getBytes());
	}

	protected String convertByteArrayToStringUsingBase64(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(bytes);
	}

	protected byte[] convertStringToByteArrayUsingBase64(String stringData) {
		return DatatypeConverter.parseBase64Binary(stringData);
	}

	public String encode(String urlStringToEncode) {
		try {
			return URLEncoder.encode(urlStringToEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String decode(String urlStringToEncode) {
		try {
			return URLDecoder.decode(urlStringToEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
