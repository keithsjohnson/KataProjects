package uk.co.keithsjohnson.xmltojsonconverter.lambda;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.keithsjohnson.sqs.lambda.SQSMessagesHandlerLambda;

public class SQSMessageHandlerLamdaTest {

	private static final String XMLS3 = "<report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>";
	private SQSMessagesHandlerLambda testSubject;

	private TestContext testContext = new TestContext();

	@Before
	public void setUp() {
		testSubject = new SQSMessagesHandlerLambda();
	}

	@Test
	@Ignore
	public void shouldSendSQSRequestMessagesToQueue() {
		// SQSMessageRequest sqsMessageRequest = new SQSMessageRequest(XMLS3);

		// SQSMessageRequestsList sqsMessageRequestsList = new
		// SQSMessageRequestsList(Arrays.asList(sqsMessageRequest));
		// SQSMessageResponsesList sqsMessageResponsesList = testSubject
		// .handleSendSQSRequestMessagesToQueue(sqsMessageRequestsList,
		// testContext);
	}
}
