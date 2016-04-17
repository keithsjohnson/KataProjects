package uk.co.keithsjohnson.sqs.lambda.api;

public class SqsMessageRequest {
	private String id;

	private String sqsRequest;

	private String snsTopicArn;

	private String subject;

	public SqsMessageRequest() {
	}

	public SqsMessageRequest(String id, String sqsRequest, String snsTopicArn, String subject) {
		this.id = id;
		this.sqsRequest = sqsRequest;
		this.snsTopicArn = snsTopicArn;
		this.subject = subject;
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

	public String getSnsTopicArn() {
		return snsTopicArn;
	}

	public void setSnsTopicArn(String snsTopicArn) {
		this.snsTopicArn = snsTopicArn;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "SqsMessageRequest [id=" + id + ", sqsRequest=" + sqsRequest + ", snsTopicArn=" + snsTopicArn
				+ ", subject=" + subject + "]";
	}
}
