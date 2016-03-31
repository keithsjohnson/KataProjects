package uk.co.keithsjohnson.jasperreports.lambda.api;

public class JasperReportJSONResponse {
	private String pdfFilename;

	private String pdfBase64String;

	public JasperReportJSONResponse() {
	}

	public JasperReportJSONResponse(String pdfFilename, String pdfBase64String) {
		this.pdfFilename = pdfFilename;
		this.pdfBase64String = pdfBase64String;
	}

	public String getPdfFilename() {
		return pdfFilename;
	}

	public void setPdfFilename(String pdfFilename) {
		this.pdfFilename = pdfFilename;
	}

	public String getPdfBase64String() {
		return pdfBase64String;
	}

	public void setPdfBase64String(String pdfBase64String) {
		this.pdfBase64String = pdfBase64String;
	}

	@Override
	public String toString() {
		return "JasperReportJSONResponse [pdfFilename=" + pdfFilename + ", pdfBase64String=" + pdfBase64String + "]";
	}
}
