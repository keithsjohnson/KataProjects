Jasper Reports AWS Lambda Simple Example
----------------------------------------

Author
------
Keith Johnson

Source
------
See my KataProjects repo on GitHub:
https://github.com/keithsjohnson/KataProjects

in the uk.co.keithsjohnson.jasperreports.lambda project:
https://github.com/keithsjohnson/KataProjects/tree/master/uk.co.keithsjohnson.jasperreports.lambda

Introduction
------------

Warning this project is still in progress and only an example of what AWS Lambda can be used for.

This project is a simple example for using an AWS Lambda written in Java to produce Jasper Reports.

The example use AWS S3 for Input and Output using 3 buckets:

1. Jasper Report XML Templates Bucket to store the .jrxml files containing the report designs.
   These .jrxml files are created using iReport.
   This bucket is current called jasperreports-jrxml
   
2. Jasper Reports XML Request Data files containing the request xml data files.
   This bucket is current called jasperreports-xml-requests

3. Jasper Reports Generated PDF files containing the output files.  
   This bucket is current called jasperreports-generated-pdf

The process is as follows:

1. Jasper Report XML Templates are uploaded to the jasperreports-jrxml bucket.

2. XML Request Files are uploaded to the jasperreports-xml-requests bucket.

3. When a file is uploaded to jasperreports-xml-requests bucket, the UploadPDFGenerationXmlFileLamda is started automatically by AWS passing an S3Event and context.

4. The UploadPDFGenerationXmlFileLamda retrieves the XML Request File information from the S3Event and reads the contents of the XML Request File from S3.

5. The UploadPDFGenerationXmlFileLamda processes the XML Request File to determine the following information:
   a. The xml file must have a top level element called 'report'.
   b. The report element has attribute 'xml-select-expression' with the select expression required by Jasper Reports e.g. /report/city.
   c. The report element has attribute 'jrxml-location' with the bucket name containing the .jrxml files e.g. jasperreports-jrxml.
   d. The report element has attribute 'jrxml' with the Jasper Report .jrxml filename for the report to be created e.g. s3-report.xml.
   e. The report element has attribute 'pdf-location' with the bucket name for where to write the generated pdf files e.g. jasperreports-generated-pdf.
   g. The report element has attribute 'pdf' the filename for the generated xml file. e.g. s3-report.pdf. Note: the processes append a UUID e.g. s3-report-8fe22921-57b4-4ec2-a542-b1381e8637f3.pdf
   
6. The UploadPDFGenerationXmlFileLamda process Reads the .jrxml file from the bucket and compiles the report.

7. The UploadPDFGenerationXmlFileLamda process Creates a DataSource for the XML Request File containing the Report Data.

8. The UploadPDFGenerationXmlFileLamda process Generates the PDF as a Byte Array using the compiled report and data.

9. The UploadPDFGenerationXmlFileLamda process Writes the PDF Byte Array to S3 bucket jasperreports-generated-pdf.

10. The UploadPDFGenerationXmlFileLamda process Deletes the XML Request File from S3.

Test Data
---------
The directory src/main/java/resources/static contains 2 simple examples of a .jrxml files report.jrxml and XMLDSReport.xml.

The directory src/main/java/resources/static contains 20 simple examples of XML Request Files s3-report*.xml and s2-simple*.xml.

Build Information
-----------------
The build uses Gradle and use the following command from project home directory:
gradlew clean build

Lambda Handler Information
--------------------------
Name: UploadPDFGenerationXmlFileLamda
Handler: uk.co.keithsjohnson.jasperreports.lambda.UploadPDFGenerationXmlFileLamda::handleUploadPDFGenerationXmlFile
Role: <Pick a role that has read and write access to S3>
Runtime: Java 8
Memory: 512Mb
Timeout: 1 min
VPC: No VPC
Upload file: build/distributions/uk.co.keithsjohnson.jasperreports.lambda-1.0.zip


Name: PDFGenerationJSONStringLamda
Handler: uk.co.keithsjohnson.jasperreports.lambda.UploadPDFGenerationXmlFileLamda::handleUploadPDFGenerationJSONString
... as above example

Note:
-----
Can also be used locally from JUnit using PDFGeneratorTest.java

ToDo
----
Add Error Handling to produce good information when buckets or files do not exist.

Make more flexible to not rely on top level report xml element.

Try more detailed Jasper Reports - they are very simple at the moment.

Cache compiled jrxml reports to help with performance.

Need to add return of PDF in JSON result.

Notes
-----
Javascript to output pdf in base64 String to browser:
http://stackoverflow.com/questions/11415665/save-base64-string-as-pdf-at-client-side-with-javascript
---
window.open("data:application/pdf;base64," + Base64.encode(out));
---
HTML:
<!doctype>
<html>
<head>
   <title>jsPDF</title>
   <script type="text/javascript" src="../libs/base64.js"></script>
   <script type="text/javascript" src="../libs/sprintf.js"></script>
   <script type="text/javascript" src="../jspdf.js"></script>

       <script type="text/javascript">

        function demo1() {
            jsPDF.init();
            jsPDF.addPage();
            jsPDF.text(20, 20, 'Hello world!');
            jsPDF.text(20, 30, 'This is client-side Javascript, pumping out a PDF.');

            // Making Data URI
            var out = jsPDF.output();
            var url = 'data:application/pdf;base64,' + Base64.encode(out);

            document.location.href = url;
         }
    </script>
</head>
<body>

<a href="javascript:demo1()">Run Code</a>

</body>
</html>
