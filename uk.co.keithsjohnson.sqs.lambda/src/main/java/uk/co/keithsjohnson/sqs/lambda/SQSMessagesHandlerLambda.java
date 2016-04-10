package uk.co.keithsjohnson.sqs.lambda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.amazonaws.services.lambda.runtime.Context;

import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageRequestsList;
import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageResponse;
import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageResponsesList;
import uk.co.keithsjohnson.sqs.lambda.service.SQSMessageRequestSender;

public class SQSMessagesHandlerLambda {

	private static final String START = "Start";

	private static final String END = "End";

	private static final String DASHED_LINE = "-----------------------------------";

	private static final String EUROPE_LONDON_TIMEZONE = "Europe/London";

	private static final String DD_MMM_YYYY_HH_MM_SS_S = "dd MMM yyyy HH:mm:ss.S";

	private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DD_MMM_YYYY_HH_MM_SS_S);

	private final SQSMessageRequestSender sqsMessageRequestSender;

	public SQSMessagesHandlerLambda() {
		sqsMessageRequestSender = new SQSMessageRequestSender();
	}

	public SqsMessageResponsesList handleSendSQSRequestMessagesToQueue(
			SqsMessageRequestsList sqsMessageRequestsList,
			Context context) {

		Consumer<String> logUKFormattedDateMessage = sendLogToContext(context);

		Stream
				.of(DASHED_LINE,
						START,
						sqsMessageRequestsList.toString(),
						DASHED_LINE)
				.forEach(logUKFormattedDateMessage);

		SqsMessageResponsesList sqsMessageResponsesList = sqsMessageRequestSender
				.sendSQSMessageRequest(sqsMessageRequestsList);

		SqsMessageResponse sqsMessageResponse = new SqsMessageResponse();

		Stream
				.of(sqsMessageResponse.toString(),
						END,
						DASHED_LINE)
				.forEach(logUKFormattedDateMessage);
		return sqsMessageResponsesList;
	}

	protected Consumer<String> sendLogToContext(Context context) {
		Consumer<String> logUKFormattedDateMessage = (message) -> {
			context.getLogger()
					.log(LocalDateTime.now(ZoneId.of(EUROPE_LONDON_TIMEZONE)).format(DATE_TIME_FORMATTER) + ": "
							+ message);
		};
		return logUKFormattedDateMessage;
	}

	protected String getNowAsFormatedUKDateTimeString() {
		return LocalDateTime.now(ZoneId.of(EUROPE_LONDON_TIMEZONE)).format(DATE_TIME_FORMATTER);
	}

	BiConsumer<String, Context> contextLogUKFormattedDateMessage = (message, context) -> {
		System.out.println(
				LocalDateTime.now(ZoneId.of(EUROPE_LONDON_TIMEZONE)).format(DATE_TIME_FORMATTER) + ": " + message);
	};
}
