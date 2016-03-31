package uk.co.keithsjohnson.jasperreports.lambda.api;

import java.util.List;

public class JasperReportJSONResponseList {
	private List<JasperReportJSONResponse> report;

	public JasperReportJSONResponseList() {
	}

	public JasperReportJSONResponseList(List<JasperReportJSONResponse> report) {
		this.report = report;
	}

	public List<JasperReportJSONResponse> getReport() {
		return report;
	}

	public void setReport(List<JasperReportJSONResponse> report) {
		this.report = report;
	}

}
