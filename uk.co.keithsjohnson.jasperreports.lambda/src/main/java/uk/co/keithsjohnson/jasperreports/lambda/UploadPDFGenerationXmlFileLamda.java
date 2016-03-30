package uk.co.keithsjohnson.jasperreports.lambda;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportIOProcessor;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportJSONRequest;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportJSONResponse;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;
import uk.co.keithsjohnson.jasperreports.lambda.service.JasperReportCreatorImpl;
import uk.co.keithsjohnson.jasperreports.lambda.service.JasperReportXmlDataProcessor;
import uk.co.keithsjohnson.jasperreports.lambda.service.S3JasperReportIOProcessorImpl;

public class UploadPDFGenerationXmlFileLamda {

	private static final String DD_MMM_YYYY_HH_MM_SS_S = "dd MMM yyyy HH:mm:ss.S";

	private static final String EUROPE_LONDON_TIMEZONE = "Europe/London";

	private final JasperReportIOProcessor jasperReportProcessor;

	private final JasperReportCreatorImpl jasperReportCreatorImpl;

	public UploadPDFGenerationXmlFileLamda() {
		jasperReportProcessor = new S3JasperReportIOProcessorImpl(new JasperReportXmlDataProcessor());
		jasperReportCreatorImpl = new JasperReportCreatorImpl();
	}

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MMM_YYYY_HH_MM_SS_S);

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

	public JasperReportJSONResponse handleUploadPDFGenerationJSONString(JasperReportJSONRequest jasperReportJSONRequest,
			final Context context) {
		String[] xmlDataArray = jasperReportJSONRequest.getXmlRequestData();

		List<String> xmlDataList = Arrays.asList(xmlDataArray);

		String[] pdfFilename = xmlDataList
				.stream()
				.map(xmlData -> processXmlData(context, xmlData))
				.toArray(String[]::new);

		return new JasperReportJSONResponse(pdfFilename);
	}

	protected String processXmlData(Context context, String xmlData) {
		context.getLogger().log(xmlData);
		JasperReportRequestModel jasperReportRequestModel = jasperReportProcessor.getXmlContentsForString(xmlData);
		return processJasperReportRequestModel(context, jasperReportRequestModel);
	}

	protected String processS3EventNotificationRecord(S3EventNotificationRecord s3EventNotificationRecord,
			Context context) {

		String xmlLocation = s3EventNotificationRecord.getS3().getBucket().getName();

		String xmlName = s3EventNotificationRecord.getS3().getObject().getKey();
		context.getLogger().log("xmlLocation=" + xmlLocation + ", xmlName=" + xmlName);
		context.getLogger().log("-----------------------------------");

		JasperReportRequestModel jasperReportRequestModel = jasperReportProcessor
				.getJasperReportRequestModel(xmlLocation, xmlName);

		jasperReportProcessor.deleteXmlFile(jasperReportRequestModel.getXmlLocation(),
				jasperReportRequestModel.getXmlName());

		return processJasperReportRequestModel(context, jasperReportRequestModel);
	}

	protected String processJasperReportRequestModel(Context context,
			JasperReportRequestModel jasperReportRequestModel) {
		context.getLogger().log(jasperReportRequestModel.toString());
		context.getLogger().log("-----------------------------------");

		byte[] pdfBytes = jasperReportCreatorImpl.processJasperReportRequest(jasperReportRequestModel);

		jasperReportProcessor.writePdf(jasperReportRequestModel, pdfBytes);

		String endMessage = "END: " + jasperReportRequestModel.getJasperReportXmlDataModel().getPdfNameWithUUID()
				+ " at " + getNowAsFormatedUKDateTimeString();
		context.getLogger().log(endMessage);
		context.getLogger().log("-----------------------------------");
		return jasperReportRequestModel.getJasperReportXmlDataModel().getPdfNameWithUUID();
	}

	protected String getNowAsFormatedUKDateTimeString() {
		return LocalDateTime.now(ZoneId.of(EUROPE_LONDON_TIMEZONE)).format(formatter);
	}

}
