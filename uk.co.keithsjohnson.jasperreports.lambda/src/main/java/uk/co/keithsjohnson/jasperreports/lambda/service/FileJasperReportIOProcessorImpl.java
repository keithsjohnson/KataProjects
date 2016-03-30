package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportIOProcessor;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportXmlDataModel;

public class FileJasperReportIOProcessorImpl implements JasperReportIOProcessor {

	private final JasperReportXmlDataProcessor jasperReportXmlDataProcessor;

	public FileJasperReportIOProcessorImpl(JasperReportXmlDataProcessor jasperReportXmlDataProcessor) {
		this.jasperReportXmlDataProcessor = jasperReportXmlDataProcessor;
	}

	@Override
	public JasperReportRequestModel getJasperReportRequestModel(String xmlLocation, String xmlName) {
		try {
			String xmlContents = getStringContentsForFilename(xmlLocation + "/" + xmlName);

			JasperReportRequestModel jasperReportRequestModel = getXmlContentsForLocationAndName(xmlLocation, xmlName,
					xmlContents);

			return jasperReportRequestModel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public JasperReportRequestModel getXmlContentsForString(String xmlContents) {
		return getXmlContentsForLocationAndName(null, null, xmlContents);
	}

	protected JasperReportRequestModel getXmlContentsForLocationAndName(String xmlLocation, String xmlName,
			String xmlContents) {
		JasperReportXmlDataModel jasperReportXmlDataModel = jasperReportXmlDataProcessor
				.getJasperReportXmlDataModelForRequest(xmlContents);

		String jrxmlContents = getStringContentsForFilename(
				jasperReportXmlDataModel.getJrxmlLocation() + "/" + jasperReportXmlDataModel.getJrxmlName());

		JasperReportRequestModel jasperReportRequestModel = new JasperReportRequestModel(xmlLocation, xmlName,
				xmlContents, jasperReportXmlDataModel, jrxmlContents);
		return jasperReportRequestModel;
	}

	@Override
	public void writePdf(JasperReportRequestModel jasperReportRequestModel, byte[] pdfBytes) {
		try {
			String pdfFilename = jasperReportRequestModel.getJasperReportXmlDataModel().getPdfLocation() + "/"
					+ jasperReportRequestModel.getJasperReportXmlDataModel().getPdfNameWithUUID();
			final Path pdfPath = Paths.get(pdfFilename);
			Files.deleteIfExists(pdfPath);
			Files.write(pdfPath, pdfBytes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getStringContentsForFilename(String inputFilename) {
		try {
			final Path path = Paths.get(inputFilename);
			return new String(Files.readAllBytes(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getInputStreamForString(String inputString) {
		try {
			return new FileInputStream(inputString);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteXmlFile(String xmlLocation, String xmlName) {
		throw new RuntimeException("Not Implemented for FileJasperReportIOProcessorImpl.");
	}
}
