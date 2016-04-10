package uk.co.keithsjohnson.sqs.lambda.api;

public class SqsMessageResponse {
	private String id;

	private String sqsResponse;

	public SqsMessageResponse() {
	}

	public SqsMessageResponse(String id, String sqsResponse) {
		this.id = id;
		this.sqsResponse = sqsResponse;
	}

	public String getSqsResponse() {
		return sqsResponse;
	}

	public void setSqsResponse(String sqsResponse) {
		this.sqsResponse = sqsResponse;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SQSMessageResponse [id=" + id + ", sqsResponse=" + sqsResponse + "]";
	}
}
