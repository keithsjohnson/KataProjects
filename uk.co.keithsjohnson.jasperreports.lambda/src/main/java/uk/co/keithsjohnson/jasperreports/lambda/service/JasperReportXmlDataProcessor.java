package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportXmlDataModel;

public class JasperReportXmlDataProcessor {

	public JasperReportXmlDataProcessor() {
	}

	public JasperReportXmlDataModel getJasperReportXmlDataModelForRequest(String xmlContents) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContents));

			Document xmlDocument = dBuilder.parse(is);

			NodeList reportNodeList = xmlDocument.getElementsByTagName("report");
			System.out.println(reportNodeList.getLength());

			Element reportElement = (Element) reportNodeList.item(0);

			String xmlSelectExpression = reportElement.getAttribute("xml-select-expression");
			String jrxmlLocation = reportElement.getAttribute("jrxml-location");
			String jrxmlName = reportElement.getAttribute("jrxml");
			String pdfLocation = reportElement.getAttribute("pdf-location");
			String pdfName = reportElement.getAttribute("pdf");

			JasperReportXmlDataModel jasperReportXmlDataModel = new JasperReportXmlDataModel(xmlSelectExpression,
					jrxmlLocation, jrxmlName, pdfLocation, pdfName);
			return jasperReportXmlDataModel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
