package uk.co.keithsjohnson.jasperreports.lambda;

import org.junit.Before;
import org.junit.Test;

public class PDFGeneratorTest {

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
}
