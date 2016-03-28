package uk.co.keithsjohnson.jasperreports.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;
import uk.co.keithsjohnson.jasperreports.lambda.service.FileJasperReportProcessorImpl;
import uk.co.keithsjohnson.jasperreports.lambda.service.JasperReportProcessorImpl;
import uk.co.keithsjohnson.jasperreports.lambda.service.S3JasperReportProcessorImpl;

public class UploadPDFGenerationXmlFileLamda {

	private final static String CITIES_SELECT_EXPRESSION = "/report/city";

	private final S3JasperReportProcessorImpl s3JasperReportProcessorImpl;

	private final JasperReportProcessorImpl jasperReportProcessorImpl;

	private final FileJasperReportProcessorImpl fileJasperReportProcessorImpl;

	public UploadPDFGenerationXmlFileLamda() {
		s3JasperReportProcessorImpl = new S3JasperReportProcessorImpl();
		jasperReportProcessorImpl = new JasperReportProcessorImpl();
		fileJasperReportProcessorImpl = new FileJasperReportProcessorImpl();
	}

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss.S");

	public String handleUploadPDFGenerationXmlFile(S3Event s3event, Context context) throws IOException {

		context.getLogger().log("-----------------------------------");
		context.getLogger().log("START: " + getNowAsFormatedUKDateTimeString());

		context.getLogger().log(s3event.toJson());
		context.getLogger().log("-----------------------------------");

		s3event.getRecords()
				.stream()
				.forEach(s3EventNotificationRecord -> processS3EventNotificationRecord(s3EventNotificationRecord,
						context));

		return "OK";
	}

	protected String processS3EventNotificationRecord(S3EventNotificationRecord s3EventNotificationRecord,
			Context context) {

		String xmlLocation = s3EventNotificationRecord.getS3().getBucket().getName();

		String xmlName = s3EventNotificationRecord.getS3().getObject().getKey();
		context.getLogger().log("xmlLocation=" + xmlLocation + ", xmlName=" + xmlName);
		context.getLogger().log("-----------------------------------");

		JasperReportRequestModel jasperReportRequestModel = s3JasperReportProcessorImpl
				.getJasperReportRequestModel(context, xmlLocation, xmlName);

		byte[] pdfBytes = jasperReportProcessorImpl.processJasperReportRequest(jasperReportRequestModel);

		s3JasperReportProcessorImpl.writePdf(jasperReportRequestModel, pdfBytes);

		s3JasperReportProcessorImpl.deleteXmlFile(jasperReportRequestModel.getXmlLocation(),
				jasperReportRequestModel.getXmlName());

		context.getLogger()
				.log("END: " + jasperReportRequestModel.getPdfName() + " at " + getNowAsFormatedUKDateTimeString());
		context.getLogger().log("-----------------------------------");
		return "OK";
	}

	protected String getNowAsFormatedUKDateTimeString() {
		return LocalDateTime.now(ZoneId.of("Europe/London")).format(formatter);
	}

	public void generateReport() {
		String inputJRXMLFilename = "src/main/resources/static/report.jrxml";
		String inputXMLDataFilename = "src/main/resources/static/report.xml";
		String outputPDFFilename = "src/main/resources/static/report.pdf";

		InputStream jrxmlInputStream = fileJasperReportProcessorImpl.getJRXMLInputStream(inputJRXMLFilename);
		InputStream xmlRequestDataInputStream = fileJasperReportProcessorImpl
				.getXMLDataInputStream(inputXMLDataFilename);

		byte[] pdfBytes = jasperReportProcessorImpl.generatePdfReportAsBytes(jrxmlInputStream,
				xmlRequestDataInputStream, CITIES_SELECT_EXPRESSION);

		fileJasperReportProcessorImpl.writeGeneratedPdfToFile(outputPDFFilename, pdfBytes);
	}

	protected InputStream getInputStreamFromString(String content) {
		try {
			return IOUtils.toInputStream(content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
