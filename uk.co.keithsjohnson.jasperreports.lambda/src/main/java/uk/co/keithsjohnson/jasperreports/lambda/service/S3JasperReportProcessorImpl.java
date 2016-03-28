package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;

public class S3JasperReportProcessorImpl {

	private final static String JRXML_S3_BUCKET = "jasperreports-jrxml";

	private final static String GENERATED_PDF_S3_BUCKET = "jasperreports-generated-pdf";

	private final static String REPORT_JRXML = "report.jrxml";

	private final static String CITIES_SELECT_EXPRESSION = "/report/city";

	private final AmazonS3 s3Client;

	public S3JasperReportProcessorImpl() {
		s3Client = new AmazonS3Client();
	}

	public JasperReportRequestModel getJasperReportRequestModel(Context context, String xmlLocation,
			String xmlName) {

		s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));

		S3Object s3Object = s3Client.getObject(new GetObjectRequest(xmlLocation, xmlName));

		context.getLogger().log(s3Object.toString());
		context.getLogger().log("-----------------------------------");

		String xmlContents = getFileContentsFromS3AsString(s3Client, xmlLocation, xmlName, context);
		context.getLogger().log(xmlContents);

		String jrxmlLocation = JRXML_S3_BUCKET;
		String jrxmlName = REPORT_JRXML;
		String pdfLocation = GENERATED_PDF_S3_BUCKET;
		String xmlSelectExpression = CITIES_SELECT_EXPRESSION;

		String jrxmlContents = getFileContentsFromS3AsString(s3Client, jrxmlLocation, jrxmlName, context);
		context.getLogger().log(jrxmlContents);

		String pdfName = xmlName.substring(0, xmlName.length() - 4) + "-" + UUID.randomUUID().toString() + ".pdf";

		JasperReportRequestModel jasperReportRequestModel = new JasperReportRequestModel(xmlLocation, xmlName,
				xmlContents, xmlSelectExpression, jrxmlLocation, jrxmlName, jrxmlContents, pdfLocation, pdfName);

		return jasperReportRequestModel;
	}

	protected String getFileContentsFromS3AsString(AmazonS3 s3Client, String bucket, String key, Context context) {
		S3Object s3Object = s3Client.getObject(bucket, key);

		InputStreamReader in = new InputStreamReader(s3Object.getObjectContent());

		BufferedReader reader = new BufferedReader(in);

		int length = (int) s3Object.getObjectMetadata().getContentLength();

		StringBuilder fileContents = new StringBuilder(length);

		String line;

		try {
			while ((line = reader.readLine()) != null) {
				fileContents.append(line);
			}
		} catch (IOException e) {
			context.getLogger().log(e.toString());
			throw new RuntimeException(e);
		}

		return fileContents.toString();
	}

	public void writePdf(JasperReportRequestModel jasperReportRequestModel, byte[] pdfBytes) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/pdf");
		metadata.setContentDisposition("attachment; filename=\"" + jasperReportRequestModel.getPdfName() + "\"");
		metadata.setContentLength(pdfBytes.length);

		InputStream pdfInputStream = getInputStreamFromByteArray(pdfBytes);

		s3Client.putObject(GENERATED_PDF_S3_BUCKET, jasperReportRequestModel.getPdfName(), pdfInputStream, metadata);

	}

	protected InputStream getInputStreamFromByteArray(byte[] content) {
		return new ByteArrayInputStream(content);
	}

	public void deleteXmlFile(String xmlLocation, String xmlName) {
		s3Client.deleteObject(xmlLocation, xmlName);
	}
}
