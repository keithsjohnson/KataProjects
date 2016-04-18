package uk.co.keithsjohnson.sqs.lambda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.BatchResultErrorEntry;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageBatchResultEntry;

import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageRequest;
import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageRequestsList;
import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageResponse;
import uk.co.keithsjohnson.sqs.lambda.api.SqsMessageResponsesList;

public class SQSMessageRequestSender {

	private static final String SEND = "send";

	private static final String SEND_SNS = "sendSNS";

	private final AmazonSQS sqsClient;

	private final AmazonSNS snsClient;

	public SQSMessageRequestSender() {
		sqsClient = new AmazonSQSClient();
		snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.EU_Ireland.toAWSRegion());
	}

	public SqsMessageResponsesList sendSQSMessageRequest(
			SqsMessageRequestsList sqsMessageRequestsList) {

		if (SEND.equalsIgnoreCase(sqsMessageRequestsList.getSqsRequestType())) {
			SendMessageBatchRequest sendMessageBatchRequest = convertSQSMessageRequestListToSendMessageBatchResquest(
					sqsMessageRequestsList);

			SendMessageBatchResult sendMessageBatchResult = sqsClient.sendMessageBatch(sendMessageBatchRequest);

			List<SendMessageBatchResultEntry> sendMessageBatchResultEntry = sendMessageBatchResult.getSuccessful();
			SqsMessageResponsesList sqsMessageResponsesList = convertSendMessageBatchResultToSQSMessageResponseList(
					sqsMessageRequestsList.getSqsRequestType(), sendMessageBatchResult, sendMessageBatchResultEntry);

			return sqsMessageResponsesList;
		} else if (SEND_SNS.equalsIgnoreCase(sqsMessageRequestsList.getSqsRequestType())) {

			List<PublishRequest> publishRequests = convertSQSMessageRequestListToPublishRequest(sqsMessageRequestsList);

			List<PublishResult> publishResults = publishRequests
					.stream()
					.map(publishRequest -> snsClient.publish(publishRequest))
					.collect(Collectors.toList());

			List<SqsMessageResponse> sqsMessageResponses = publishResults
					.stream()
					.map(mapPublishResultToSqsMessageResponse)
					.collect(Collectors.toList());

			SqsMessageResponsesList sqsMessageResponsesList = new SqsMessageResponsesList(
					sqsMessageRequestsList.getSqsRequestType(), sqsMessageResponses);
			return sqsMessageResponsesList;
		} else {
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
			receiveMessageRequest.setQueueUrl(sqsMessageRequestsList.getSqsUrl());
			receiveMessageRequest.setMaxNumberOfMessages(10);
			receiveMessageRequest.setWaitTimeSeconds(1);

			ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);

			List<Message> messages = receiveMessageResult.getMessages();
			List<SqsMessageResponse> sqsMessageResponses = messages
					.stream()
					.map(mapMessageToSQSMessageResponse)
					.collect(Collectors.toCollection(ArrayList::new));

			SqsMessageResponsesList sqsMessageResponsesList = new SqsMessageResponsesList(
					sqsMessageRequestsList.getSqsRequestType(), sqsMessageResponses);

			Consumer<SqsMessageResponse> deleteMessageFromQueue = (message) -> {
				sqsClient.deleteMessage(sqsMessageRequestsList.getSqsUrl(), message.getId());
			};

			List<SqsMessageResponse> sqsMessageResponsesToDelete = sqsMessageResponsesList.getSqsMessageResponsesList();
			sqsMessageResponsesToDelete
					.stream()
					.forEach(deleteMessageFromQueue);

			return sqsMessageResponsesList;
		}
	}

	protected List<PublishRequest> convertSQSMessageRequestListToPublishRequest(
			SqsMessageRequestsList sqsMessageRequestsList) {
		SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest(
				sqsMessageRequestsList.getSqsUrl());

		List<SqsMessageRequest> sqsMessageRequestsListGeneric = sqsMessageRequestsList.getSqsMessageRequestsList();

		List<PublishRequest> publishRequests = sqsMessageRequestsListGeneric
				.stream()
				.map(mapSQSMessageRequestToPublishRequest)
				.collect(Collectors.toCollection(ArrayList::new));

		return publishRequests;
	}

	protected SendMessageBatchRequest convertSQSMessageRequestListToSendMessageBatchResquest(
			SqsMessageRequestsList sqsMessageRequestsList) {
		SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest(
				sqsMessageRequestsList.getSqsUrl());

		List<SqsMessageRequest> sqsMessageRequestsListGeneric = sqsMessageRequestsList.getSqsMessageRequestsList();

		List<SendMessageBatchRequestEntry> sendMessageBatchRequestEntries = sqsMessageRequestsListGeneric
				.stream()
				.map(mapSQSMessageRequestToSendMessageBatchRequestEntry)
				.collect(Collectors.toCollection(ArrayList::new));

		sendMessageBatchRequest.setEntries(sendMessageBatchRequestEntries);

		return sendMessageBatchRequest;
	}

	protected SqsMessageResponsesList convertSendMessageBatchResultToSQSMessageResponseList(String sqsRequestType,
			SendMessageBatchResult sendMessageBatchResult,
			List<SendMessageBatchResultEntry> sendMessageBatchResultEntry) {
		List<SqsMessageResponse> sqsMessageResponses = sendMessageBatchResultEntry
				.stream()
				.map(mapSendMessageBatchResultEntryToSQSMessageResponse)
				.collect(Collectors.toCollection(ArrayList::new));

		List<BatchResultErrorEntry> batchResultErrorEntry = sendMessageBatchResult.getFailed();
		List<SqsMessageResponse> sqsMessageResponsesErrors = batchResultErrorEntry
				.stream()
				.map(mapBatchResultErrorEntryToSQSMessageResponse)
				.collect(Collectors.toCollection(ArrayList::new));

		sqsMessageResponses.addAll(sqsMessageResponsesErrors);

		SqsMessageResponsesList sqsMessageResponsesList = new SqsMessageResponsesList(sqsRequestType,
				sqsMessageResponses);

		return sqsMessageResponsesList;
	}

	Function<SqsMessageRequest, PublishRequest> mapSQSMessageRequestToPublishRequest = new Function<SqsMessageRequest, PublishRequest>() {
		public PublishRequest apply(SqsMessageRequest sqsMessageRequest) {
			PublishRequest publishRequest = new PublishRequest(sqsMessageRequest.getSnsTopicArn(),
					sqsMessageRequest.getSqsRequest(), sqsMessageRequest.getSubject());
			System.out.println(publishRequest.toString());
			return publishRequest;
		}
	};

	Function<SqsMessageRequest, SendMessageBatchRequestEntry> mapSQSMessageRequestToSendMessageBatchRequestEntry = new Function<SqsMessageRequest, SendMessageBatchRequestEntry>() {
		public SendMessageBatchRequestEntry apply(SqsMessageRequest sqsMessageRequest) {
			SendMessageBatchRequestEntry sendMessageBatchRequestEntry = new SendMessageBatchRequestEntry(
					sqsMessageRequest.getId(), sqsMessageRequest.getSqsRequest());
			return sendMessageBatchRequestEntry;
		}
	};

	Function<SendMessageBatchResultEntry, SqsMessageResponse> mapSendMessageBatchResultEntryToSQSMessageResponse = new Function<SendMessageBatchResultEntry, SqsMessageResponse>() {
		public SqsMessageResponse apply(SendMessageBatchResultEntry sendMessageBatchResultEntry) {
			SqsMessageResponse sqsMessageResponse = new SqsMessageResponse(sendMessageBatchResultEntry.getId(),
					sendMessageBatchResultEntry.getMessageId());
			return sqsMessageResponse;
		}
	};

	Function<BatchResultErrorEntry, SqsMessageResponse> mapBatchResultErrorEntryToSQSMessageResponse = new Function<BatchResultErrorEntry, SqsMessageResponse>() {
		public SqsMessageResponse apply(BatchResultErrorEntry sendMessageBatchResultEntry) {
			SqsMessageResponse sqsMessageResponse = new SqsMessageResponse(sendMessageBatchResultEntry.getId(),
					sendMessageBatchResultEntry.getCode() + "" + sendMessageBatchResultEntry.getMessage());
			return sqsMessageResponse;
		}
	};

	Function<Message, SqsMessageResponse> mapMessageToSQSMessageResponse = new Function<Message, SqsMessageResponse>() {
		public SqsMessageResponse apply(Message message) {
			SqsMessageResponse sqsMessageResponse = new SqsMessageResponse(message.getReceiptHandle(),
					message.getBody());
			return sqsMessageResponse;
		}
	};

	Function<PublishResult, SqsMessageResponse> mapPublishResultToSqsMessageResponse = new Function<PublishResult, SqsMessageResponse>() {
		public SqsMessageResponse apply(PublishResult publishResult) {
			SqsMessageResponse sqsMessageResponse = new SqsMessageResponse(publishResult.getMessageId(), "");
			return sqsMessageResponse;
		}
	};

}
