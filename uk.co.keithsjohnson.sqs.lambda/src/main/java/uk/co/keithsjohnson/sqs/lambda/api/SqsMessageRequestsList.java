package uk.co.keithsjohnson.sqs.lambda.api;

import java.util.List;

public class SqsMessageRequestsList {

	private String sqsUrl;

	private String sqsRequestType;

	private List<SqsMessageRequest> sqsMessageRequestsList;

	public SqsMessageRequestsList() {
	}

	public SqsMessageRequestsList(String sqsUrl, String sqsRequestType,
			List<SqsMessageRequest> sqsMessageRequestsList) {
		this.sqsUrl = sqsUrl;
		this.sqsRequestType = sqsRequestType;
		this.sqsMessageRequestsList = sqsMessageRequestsList;
	}

	public List<SqsMessageRequest> getSqsMessageRequestsList() {
		return sqsMessageRequestsList;
	}

	public void setSqsMessageRequestsList(List<SqsMessageRequest> sqsMessageRequestsList) {
		this.sqsMessageRequestsList = sqsMessageRequestsList;
	}

	public String getSqsUrl() {
		return sqsUrl;
	}

	public void setSqsUrl(String sqsUrl) {
		this.sqsUrl = sqsUrl;
	}

	public String getSqsRequestType() {
		return sqsRequestType;
	}

	public void setSqsRequestType(String sqsRequestType) {
		this.sqsRequestType = sqsRequestType;
	}

	@Override
	public String toString() {
		return "SqsMessageRequestsList [sqsUrl=" + sqsUrl + ", sqsRequestType=" + sqsRequestType
				+ ", sqsMessageRequestsList=" + sqsMessageRequestsList + "]";
	}
}
