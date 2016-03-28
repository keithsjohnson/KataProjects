package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.InputStream;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

public class JasperReportProcessorImpl {

	public byte[] generatePdfReportAsBytes(InputStream jrxmlInputStream, InputStream xmlDataInputStream,
			String selectExpression) {
		try {
			JasperReport compiledJasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
			JRXmlDataSource dataSource = new JRXmlDataSource(xmlDataInputStream, selectExpression);

			HashMap<String, Object> parameters = new HashMap<String, Object>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(compiledJasperReport, parameters, dataSource);

			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
	}
}
