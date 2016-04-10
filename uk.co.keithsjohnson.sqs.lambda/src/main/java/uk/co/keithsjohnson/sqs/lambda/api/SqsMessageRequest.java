package uk.co.keithsjohnson.sqs.lambda.api;

public class SqsMessageRequest {
	private String id;

	private String sqsRequest;

	public SqsMessageRequest() {
	}

	public SqsMessageRequest(String id, String sqsRequest) {
		this.id = id;
		this.sqsRequest = sqsRequest;
	}

	public String getSqsRequest() {
		return sqsRequest;
	}

	public void setSqsRequest(String sqsRequest) {
		this.sqsRequest = sqsRequest;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SQSMessageRequest [id=" + id + ", sqsRequest=" + sqsRequest + "]";
	}
}
