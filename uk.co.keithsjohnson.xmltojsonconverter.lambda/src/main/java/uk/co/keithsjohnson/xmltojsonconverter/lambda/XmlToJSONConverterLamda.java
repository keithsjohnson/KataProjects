package uk.co.keithsjohnson.xmltojsonconverter.lambda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNS;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.co.keithsjohnson.xmltojsonconverter.lambda.api.JSONResponse;
import uk.co.keithsjohnson.xmltojsonconverter.lambda.api.XMLRequest;
import uk.co.keithsjohnson.xmltojsonconverter.lambda.service.XMLToJSONConverter;

public class XmlToJSONConverterLamda {

	private static final String DD_MMM_YYYY_HH_MM_SS_S = "dd MMM yyyy HH:mm:ss.S";

	private static final String EUROPE_LONDON_TIMEZONE = "Europe/London";

	private final XMLToJSONConverter xmlToJSONConverter;

	public XmlToJSONConverterLamda() {
		xmlToJSONConverter = new XMLToJSONConverter();
	}

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DD_MMM_YYYY_HH_MM_SS_S);

	public JSONResponse handleSQSXmlToJsonConverter(SNSEvent snsEvent, Context context) {

		context.getLogger().log("-----------------------------------");
		context.getLogger().log("START: " + getNowAsFormatedUKDateTimeString());

		List<SNSRecord> snsRecords = snsEvent.getRecords();
		context.getLogger().log("snsRecords.size()=" + snsRecords.size());
		XMLRequest xmlRequest;
		ObjectMapper mapper = new ObjectMapper();
		for (SNSRecord snsRecord : snsRecords) {
			SNS snsMessage = snsRecord.getSNS();
			context.getLogger().log("snsMessage=" + snsMessage.getMessage());

			try {
				xmlRequest = mapper.readValue(snsMessage.getMessage(), XMLRequest.class);
				context.getLogger().log(xmlRequest.toString());
				context.getLogger().log("-----------------------------------");

				JSONResponse jsonResponse = new JSONResponse(
						xmlToJSONConverter.convertXMLToJSON(xmlRequest.getXmlRequest()));
				context.getLogger().log(jsonResponse.toString());
				context.getLogger().log("-----------------------------------");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		context.getLogger().log("END: " + getNowAsFormatedUKDateTimeString());
		return new JSONResponse("OK");
	}

	public JSONResponse handleXmlToJsonConverter(XMLRequest xmlRequest, Context context) {

		context.getLogger().log("-----------------------------------");
		context.getLogger().log("START: " + getNowAsFormatedUKDateTimeString());

		context.getLogger().log(xmlRequest.toString());
		context.getLogger().log("-----------------------------------");

		JSONResponse jsonResponse = new JSONResponse(xmlToJSONConverter.convertXMLToJSON(xmlRequest.getXmlRequest()));
		context.getLogger().log(jsonResponse.toString());
		context.getLogger().log("-----------------------------------");

		context.getLogger().log("END: " + getNowAsFormatedUKDateTimeString());
		return jsonResponse;
	}

	protected String getNowAsFormatedUKDateTimeString() {
		return LocalDateTime.now(ZoneId.of(EUROPE_LONDON_TIMEZONE)).format(formatter);
	}
}
