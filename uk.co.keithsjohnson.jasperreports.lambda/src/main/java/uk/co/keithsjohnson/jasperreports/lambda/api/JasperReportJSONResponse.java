package uk.co.keithsjohnson.jasperreports.lambda.api;

import java.util.Arrays;

public class JasperReportJSONResponse {
	private String[] pdfFilename;

	public JasperReportJSONResponse() {
	}

	public JasperReportJSONResponse(String[] pdfFilename) {
		this.pdfFilename = pdfFilename;
	}

	public String[] getPdfFilename() {
		return pdfFilename;
	}

	public void setPdfFilename(String[] pdfFilename) {
		this.pdfFilename = pdfFilename;
	}

	@Override
	public String toString() {
		return "JasperReportJSONResponse [pdfFilename=" + Arrays.toString(pdfFilename) + "]";
	}
}
