package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportIOProcessor;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportXmlDataModel;

public class S3JasperReportIOProcessorImpl implements JasperReportIOProcessor {

	private final AmazonS3 s3Client;

	private final JasperReportXmlDataProcessor jasperReportXmlDataProcessor;

	public S3JasperReportIOProcessorImpl(JasperReportXmlDataProcessor jasperReportXmlDataProcessor) {
		s3Client = new AmazonS3Client();
		this.jasperReportXmlDataProcessor = jasperReportXmlDataProcessor;
	}

	@Override
	public JasperReportRequestModel getJasperReportRequestModel(String xmlLocation, String xmlName) {

		s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));

		String xmlContents = getFileContentsFromS3AsString(xmlLocation, xmlName);

		JasperReportXmlDataModel jasperReportXmlDataModel = jasperReportXmlDataProcessor
				.getJasperReportXmlDataModelForRequest(xmlContents);

		String jrxmlContents = getFileContentsFromS3AsString(jasperReportXmlDataModel.getJrxmlLocation(),
				jasperReportXmlDataModel.getJrxmlName());

		JasperReportRequestModel jasperReportRequestModel = new JasperReportRequestModel(xmlLocation, xmlName,
				xmlContents, jasperReportXmlDataModel, jrxmlContents);

		return jasperReportRequestModel;
	}

	protected String getFileContentsFromS3AsString(String bucket, String key) {
		S3Object s3Object = s3Client.getObject(bucket, key);

		InputStreamReader in = new InputStreamReader(s3Object.getObjectContent());

		BufferedReader reader = new BufferedReader(in);

		int length = (int) s3Object.getObjectMetadata().getContentLength();

		StringBuilder fileContents = new StringBuilder(length);

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				fileContents.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return fileContents.toString();
	}

	@Override
	public void writePdf(JasperReportRequestModel jasperReportRequestModel, byte[] pdfBytes) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/pdf");
		metadata.setContentDisposition(
				"attachment; filename=\"" + jasperReportRequestModel.getJasperReportXmlDataModel().getPdfNameWithUUID()
						+ "\"");
		metadata.setContentLength(pdfBytes.length);

		InputStream pdfInputStream = getInputStreamFromByteArray(pdfBytes);

		s3Client.putObject(jasperReportRequestModel.getJasperReportXmlDataModel().getPdfLocation(),
				jasperReportRequestModel.getJasperReportXmlDataModel().getPdfNameWithUUID(), pdfInputStream, metadata);
	}

	protected InputStream getInputStreamFromByteArray(byte[] content) {
		return new ByteArrayInputStream(content);
	}

	@Override
	public void deleteXmlFile(String xmlLocation, String xmlName) {
		s3Client.deleteObject(xmlLocation, xmlName);
	}
}
