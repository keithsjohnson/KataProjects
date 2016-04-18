package uk.co.keithsjohnson.xmltojsonconverter.lambda;

import org.junit.Before;
import org.junit.Test;

import uk.co.keithsjohnson.xmltojsonconverter.lambda.api.JSONResponse;
import uk.co.keithsjohnson.xmltojsonconverter.lambda.api.XMLRequest;

public class XmlToJSONConverterLamdaTest {

	private static final String XMLS3 = "<report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>";
	private XmlToJSONConverterLamda testSubject;

	private TestContext testContext = new TestContext();

	@Before
	public void setUp() {
		testSubject = new XmlToJSONConverterLamda();
	}

	@Test
	public void shouldCreateReportPdf() {
		XMLRequest xmlRequest = new XMLRequest("", XMLS3);
		JSONResponse jsonResponse = testSubject.handleXmlToJsonConverter(xmlRequest, testContext);
	}
}
