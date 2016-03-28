package uk.co.keithsjohnson.jasperreports.lambda.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

public class FileJasperReportProcessorImpl {

	public void writeGeneratedPdfToFile(String outputPDFFilename, byte[] pdfBytes) {
		try {
			final Path outputPDFPath = Paths.get(outputPDFFilename);
			Files.deleteIfExists(outputPDFPath);
			Files.write(outputPDFPath, pdfBytes, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getJRXMLInputStream(String inputJRXMLFilename) {
		try {
			final Path path = Paths.get(inputJRXMLFilename);
			String content = new String(Files.readAllBytes(path));
			return getInputStreamFromString(content);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getInputStreamFromString(String content) {
		try {
			return IOUtils.toInputStream(content, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getXMLDataInputStream(String inputXMLDataFilename) {
		try {
			return new FileInputStream(inputXMLDataFilename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
