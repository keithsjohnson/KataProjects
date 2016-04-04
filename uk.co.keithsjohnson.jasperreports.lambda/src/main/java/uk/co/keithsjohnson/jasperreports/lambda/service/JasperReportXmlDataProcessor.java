package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.co.keithsjohnson.jasperreports.lambda.api.JasperReportXmlDataModel;

public class JasperReportXmlDataProcessor {

	private static final String REPORT_ELEMENT = "report";
	private static final String XML_SELECT_EXPRESSION_ATTRIBUTE = "xml-select-expression";
	private static final String JRXML_LOCATION_ATTRIBUTE = "jrxml-location";
	private static final String JRXML_ATTRIBUTE = "jrxml";
	private static final String PDF_LOCATION_ATTRIBUTE = "pdf-location";
	private static final String PDF_ATTRIBUTE = "pdf";

	public JasperReportXmlDataProcessor() {
	}

	public JasperReportXmlDataModel getJasperReportXmlDataModelForRequest(String xmlContents) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContents));

			Document xmlDocument = dBuilder.parse(is);

			NodeList reportNodeList = xmlDocument.getElementsByTagName(REPORT_ELEMENT);
			if (reportNodeList.getLength() == 0) {
				throw new RuntimeException("'report' must be top level element.");
			}

			Element reportElement = (Element) reportNodeList.item(0);

			String xmlSelectExpression = getAttributeFromElement(reportElement, XML_SELECT_EXPRESSION_ATTRIBUTE);
			String jrxmlLocation = getAttributeFromElement(reportElement, JRXML_LOCATION_ATTRIBUTE);
			String jrxmlName = getAttributeFromElement(reportElement, JRXML_ATTRIBUTE);
			String pdfLocation = getAttributeFromElement(reportElement, PDF_LOCATION_ATTRIBUTE);
			String pdfName = getAttributeFromElement(reportElement, PDF_ATTRIBUTE);

			JasperReportXmlDataModel jasperReportXmlDataModel = new JasperReportXmlDataModel(xmlSelectExpression,
					jrxmlLocation, jrxmlName, pdfLocation, pdfName);
			return jasperReportXmlDataModel;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String getAttributeFromElement(Element reportElement, String xmlAttribute) {
		String xmlSelectExpression = reportElement.getAttribute(xmlAttribute);
		if (StringUtils.isBlank(xmlSelectExpression)) {
			throw new RuntimeException("'" + xmlAttribute + "' attribute is missing.");
		}
		return xmlSelectExpression;
	}
}
