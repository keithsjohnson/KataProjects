package uk.co.keithsjohnson.jasperreports.lambda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

public class UploadPDFGenerationXmlFileLamdaTest {

	private PDFGenerator testSubject;

	private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss.S");

	@Before
	public void setUp() {
		testSubject = new PDFGenerator();
	}

	@Test
	public void shouldCreatePdf() {
		testSubject.generateReport();
	}

	@Test
	public void shouldCreateLocalTime() {
		String now = LocalDateTime.now(ZoneId.of("Europe/London")).format(formatter);
		System.out.println(now);
	}
}
