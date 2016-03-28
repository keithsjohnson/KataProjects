package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportRequestModel;

public class JasperReportProcessorImpl {

	public byte[] processJasperReportRequest(JasperReportRequestModel jasperReportRequestModel) {
		InputStream xmlRequestDataInputStream = getInputStreamFromString(jasperReportRequestModel.getXmlContents());

		InputStream jrxmlInputStream = getInputStreamFromString(jasperReportRequestModel.getJrxmlContents());

		return generatePdfReportAsBytes(jrxmlInputStream,
				xmlRequestDataInputStream, jasperReportRequestModel.getXmlSelectExpression());
	}

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

	protected InputStream getInputStreamFromString(String content) {
		try {
			return IOUtils.toInputStream(content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
