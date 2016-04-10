package uk.co.keithsjohnson.sqs.lambda.api;

import java.util.List;

public class SqsMessageResponsesList {
	private String sqsResponseType;

	private List<SqsMessageResponse> sqsMessageResponsesList;

	public SqsMessageResponsesList() {
	}

	public SqsMessageResponsesList(String sqsResponseType, List<SqsMessageResponse> sqsMessageResponsesList) {
		this.sqsResponseType = sqsResponseType;
		this.sqsMessageResponsesList = sqsMessageResponsesList;
	}

	public String getSqsResponseType() {
		return sqsResponseType;
	}

	public void setSqsResponseType(String sqsResponseType) {
		this.sqsResponseType = sqsResponseType;
	}

	public List<SqsMessageResponse> getSqsMessageResponsesList() {
		return sqsMessageResponsesList;
	}

	public void setSqsMessageResponsesList(List<SqsMessageResponse> sqsMessageResponsesList) {
		this.sqsMessageResponsesList = sqsMessageResponsesList;
	}

	@Override
	public String toString() {
		return "SqsMessageResponsesList [sqsResponseType=" + sqsResponseType + ", sqsMessageResponsesList="
				+ sqsMessageResponsesList + "]";
	}
}
