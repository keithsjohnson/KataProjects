package uk.co.keithsjohnson.jasperreports.lambda;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PDFGeneratorTest {

	private static final String BASE64File = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48cmVwb3J0IHR5cGU9ImZpbGUiIHhtbC1zZWxlY3QtZXhwcmVzc2lvbj0iL3JlcG9ydC9jaXR5IiBqcnhtbC1sb2NhdGlvbj0ic3JjL21haW4vcmVzb3VyY2VzL3N0YXRpYyIganJ4bWw9InJlcG9ydC5qcnhtbCIgcGRmLWxvY2F0aW9uPSJzcmMvbWFpbi9yZXNvdXJjZXMvc3RhdGljIiBwZGY9ImZpbGUtcmVwb3J0LnBkZiI+PGNpdHk+PG5hbWU+TmV3IFlvcms8L25hbWU+PHBvcHVsYXRpb24+MTIwMDAwMDA8L3BvcHVsYXRpb24+PC9jaXR5PjxjaXR5PjxuYW1lPk1hbmNoZXN0ZXI8L25hbWU+PHBvcHVsYXRpb24+MTAwMDAwMDwvcG9wdWxhdGlvbj48L2NpdHk+PGNpdHk+PG5hbWU+U3Rva2U8L25hbWU+PHBvcHVsYXRpb24+MTIzNDU2PC9wb3B1bGF0aW9uPjwvY2l0eT48L3JlcG9ydD4=";
	private static final String BASE64S3 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz48cmVwb3J0IHR5cGU9InMzIiB4bWwtc2VsZWN0LWV4cHJlc3Npb249Ii9yZXBvcnQvY2l0eSIganJ4bWwtbG9jYXRpb249Imphc3BlcnJlcG9ydHMtanJ4bWwiIGpyeG1sPSJyZXBvcnQuanJ4bWwiIHBkZi1sb2NhdGlvbj0iamFzcGVycmVwb3J0cy1nZW5lcmF0ZWQtcGRmIiBwZGY9InMzLXJlcG9ydC5wZGYiPjxjaXR5PjxuYW1lPk5ldyBZb3JrPC9uYW1lPjxwb3B1bGF0aW9uPjEyMDAwMDAwPC9wb3B1bGF0aW9uPjwvY2l0eT48Y2l0eT48bmFtZT5NYW5jaGVzdGVyPC9uYW1lPjxwb3B1bGF0aW9uPjEwMDAwMDA8L3BvcHVsYXRpb24+PC9jaXR5PjxjaXR5PjxuYW1lPlN0b2tlPC9uYW1lPjxwb3B1bGF0aW9uPjEyMzQ1NjwvcG9wdWxhdGlvbj48L2NpdHk+PC9yZXBvcnQ+";
	private static final String XMLFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"file\" xml-select-expression=\"/report/city\" jrxml-location=\"src/main/resources/static\" jrxml=\"report.jrxml\" pdf-location=\"src/main/resources/static\" pdf=\"file-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>";
	private static final String XMLS3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>";
	private PDFGenerator testSubject;

	@Before
	public void setUp() {
		testSubject = new PDFGenerator();
	}

	@Test
	public void shouldCreateReportPdf() {
		testSubject.generateReport("src/main/resources/static", "file-report.xml");
	}

	@Test
	public void shouldCreateSimplePdf() {
		testSubject.generateReport("src/main/resources/static", "file-simple.xml");
	}

	// @Test
	// public void shouldError() {
	// testSubject.generateReport("<report/>");
	// }

	@Test
	public void shouldGenerateReportFromString() {
		// testSubject.generateReport(XML);
		testSubject.generateReport(BASE64File);
	}

	@Test
	public void shouldConvertXMLToBase64AndEncode() {
		String base64 = testSubject.convertXMLStringToXmlBase64String(XMLS3);
		System.out.println("base64");
		System.out.println(base64);

		String encoded = testSubject.encode(base64);
		System.out.println("encoded");
		System.out.println(encoded);

		String decoded = testSubject.decode(encoded);
		System.out.println("decoded");
		System.out.println(decoded);

		String xml = testSubject.convertXMLBase64StringToXmlString(decoded);
		System.out.println("xml");
		System.out.println(xml);

		assertEquals(XMLS3, xml);
	}
}
