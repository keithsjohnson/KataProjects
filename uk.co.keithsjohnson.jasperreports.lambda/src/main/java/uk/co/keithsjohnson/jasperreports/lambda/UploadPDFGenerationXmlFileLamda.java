package uk.co.keithsjohnson.jasperreports.lambda;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

public class UploadPDFGenerationXmlFileLamda {

	private final static String JRXML_S3_BUCKET = "jasperreports-jrxml";

	private final static String GENERATED_PDF_S3_BUCKET = "jasperreports-generated-pdf";

	private final static String REPORT_JRXML = "report.jrxml";

	private final static String CITIES_SELECT_EXPRESSION = "/cities/city";

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss.S");

	public String handleUploadPDFGenerationXmlFile(S3Event s3event, Context context) throws IOException {

		context.getLogger().log("-----------------------------------");
		context.getLogger().log("START: " + getNowAsFormatedUKDateTimeString());

		context.getLogger().log(s3event.toJson());
		context.getLogger().log("-----------------------------------");
		context.getLogger().log(s3event.toString());
		context.getLogger().log("-----------------------------------");
		S3EventNotificationRecord record = s3event.getRecords().get(0);

		String srcBucket = record.getS3().getBucket().getName();

		String srcKey = record.getS3().getObject().getKey();

		AmazonS3 s3Client = new AmazonS3Client();
		s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));

		S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));

		context.getLogger().log("srcBucket=" + srcBucket + ", srcKey=" + srcKey + ", " + s3Object.toString());
		context.getLogger().log("-----------------------------------");

		String xmlRequestFileContents = getFileContentsFromS3AsString(s3Client, srcBucket, srcKey, context);
		context.getLogger().log(xmlRequestFileContents);

		String jrxml = getFileContentsFromS3AsString(s3Client, JRXML_S3_BUCKET, REPORT_JRXML, context);
		context.getLogger().log(jrxml);

		InputStream xmlRequestDataInputStream = getInputStreamFromString(xmlRequestFileContents);
		context.getLogger().log("-----------------------------------");

		InputStream jrxmlInputStream = getInputStreamFromString(jrxml);

		byte[] pdfBytes = generatePdfReportAsBytes(jrxmlInputStream, xmlRequestDataInputStream,
				CITIES_SELECT_EXPRESSION);
		context.getLogger().log("-----------------------------------");

		InputStream pdfInputStream = getInputStreamFromByteArray(pdfBytes);
		context.getLogger().log("-----------------------------------");

		String pdfFilename = "report-" + UUID.randomUUID().toString() + ".pdf";
		context.getLogger().log(pdfFilename);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/pdf");
		metadata.setContentDisposition("attachment; filename=\"" + pdfFilename + "\"");
		metadata.setContentLength(pdfBytes.length);

		s3Client.putObject(GENERATED_PDF_S3_BUCKET, pdfFilename, pdfInputStream, metadata);
		context.getLogger().log("-----------------------------------");

		s3Client.deleteObject(srcBucket, srcKey);

		context.getLogger().log("END: " + pdfFilename + " at " + getNowAsFormatedUKDateTimeString());
		context.getLogger().log("-----------------------------------");
		return "OK " + pdfFilename;
	}

	protected String getNowAsFormatedUKDateTimeString() {
		return LocalDateTime.now(ZoneId.of("Europe/London")).format(formatter);
	}

	public String getFileContentsFromS3AsString(AmazonS3 s3Client, String bucket, String key, Context context) {
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

	public void get(InputStream is) {

	}

	public void generateReport() {
		String inputJRXMLFilename = "src/main/resources/static/report.jrxml";
		String inputXMLDataFilename = "src/main/resources/static/report.xml";
		String outputPDFFilename = "src/main/resources/static/report.pdf";

		InputStream jrxmlInputStream = getJRXMLInputStream(inputJRXMLFilename);
		InputStream xmlRequestDataInputStream = getXMLDataInputStream(inputXMLDataFilename);

		byte[] pdfBytes = generatePdfReportAsBytes(jrxmlInputStream, xmlRequestDataInputStream,
				CITIES_SELECT_EXPRESSION);

		writeGeneratedPdfToFile(outputPDFFilename, pdfBytes);
	}

	protected byte[] generatePdfReportAsBytes(InputStream jrxmlInputStream, InputStream xmlDataInputStream,
			String selectExpression) {
		try {
			JasperReport compiledJasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
			JRXmlDataSource dataSource = new JRXmlDataSource(xmlDataInputStream, selectExpression);

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledJasperReport, parameters, dataSource);

			byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);
			return pdfBytes;
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeGeneratedPdfToFile(String outputPDFFilename, byte[] pdfBytes) {
		try {
			final Path outputPDFPath = Paths.get(outputPDFFilename);
			Files.deleteIfExists(outputPDFPath);
			Files.write(outputPDFPath, pdfBytes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getJRXMLInputStream(String inputJRXMLFilename) {
		try {
			final Path path = Paths.get(inputJRXMLFilename);
			String content = new String(Files.readAllBytes(path));
			return getInputStreamFromString(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStreamFromString(String content) {
		try {
			return IOUtils.toInputStream(content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected InputStream getInputStreamFromByteArray(byte[] content) {
		return new ByteArrayInputStream(content);
	}

	protected FileInputStream getXMLDataInputStream(String inputXMLDataFilename) {
		try {
			return new FileInputStream(inputXMLDataFilename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
