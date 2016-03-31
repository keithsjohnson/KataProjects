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

Example Request JSON
{
  "xmlRequestData":
    [ "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>",
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
    ]
}

{
  "xmlRequestData": [
    { 
      "xmlRequest" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
    },
    {
      "xmlRequest" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
    }
  ]
}


Example Result JSON
{
  "report": [
    {
      "pdfFilename": "s3-report-517c3953-6caa-407e-855e-5532d8cc850d.pdf",
      "pdfBase64String": "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDI3OD4+c3RyZWFtCnicrZJNSwMxEIbv8yvmWKGsk+zmY6+KCoKiNhcRD0sba223W9eV4r83We22Fo0tDSHJkLyTZ+Ylr3BiIJWoSaIZwZmBW+B46U8Zkht+1RlHU8LxOUNGaJ6gd2RevHYtIRyWm0ncCZVWiVarTC59JrX39Rh6p5PmA2u7qOrGv0Y4/uXFh0e3j9qK/mblDiVEomVXJP+Jui5KeyCDi/8gN9XifVY0k2oeoZ00S9SW5xvt2CXeV/U0RktBEON9IvJzJxQlApdO5iOJq3l30WlSx2NOVIJQ+juewWB3W1gaqPaqmA+f7Vtj6xjGBFEsui+M1r608T6+yJwHih001TTG9w9TGE/7mZCxDJE67wz5ircM+QRaVfRfCmVuZHN0cmVhbQplbmRvYmoKMSAwIG9iago8PC9UYWJzL1MvR3JvdXA8PC9TL1RyYW5zcGFyZW5jeS9UeXBlL0dyb3VwL0NTL0RldmljZVJHQj4+L0NvbnRlbnRzIDMgMCBSL1R5cGUvUGFnZS9SZXNvdXJjZXM8PC9Db2xvclNwYWNlPDwvQ1MvRGV2aWNlUkdCPj4vUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUMgL0ltYWdlSV0vRm9udDw8L0YxIDIgMCBSPj4+Pi9QYXJlbnQgNCAwIFIvTWVkaWFCb3hbMCAwIDU5NSA4NDJdPj4KZW5kb2JqCjUgMCBvYmoKWzEgMCBSL1hZWiAwIDg1MiAwXQplbmRvYmoKMiAwIG9iago8PC9TdWJ0eXBlL1R5cGUxL1R5cGUvRm9udC9CYXNlRm9udC9IZWx2ZXRpY2EvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nPj4KZW5kb2JqCjQgMCBvYmoKPDwvS2lkc1sxIDAgUl0vVHlwZS9QYWdlcy9Db3VudCAxL0lUWFQoMi4xLjcpPj4KZW5kb2JqCjYgMCBvYmoKPDwvTmFtZXNbKEpSX1BBR0VfQU5DSE9SXzBfMSkgNSAwIFJdPj4KZW5kb2JqCjcgMCBvYmoKPDwvRGVzdHMgNiAwIFI+PgplbmRvYmoKOCAwIG9iago8PC9OYW1lcyA3IDAgUi9UeXBlL0NhdGFsb2cvUGFnZXMgNCAwIFIvVmlld2VyUHJlZmVyZW5jZXM8PC9QcmludFNjYWxpbmcvQXBwRGVmYXVsdD4+Pj4KZW5kb2JqCjkgMCBvYmoKPDwvTW9kRGF0ZShEOjIwMTYwMzMxMjE1NjM0WikvQ3JlYXRvcihKYXNwZXJSZXBvcnRzIExpYnJhcnkgdmVyc2lvbiA2LjIuMCkvQ3JlYXRpb25EYXRlKEQ6MjAxNjAzMzEyMTU2MzRaKS9Qcm9kdWNlcihpVGV4dCAyLjEuNyBieSAxVDNYVCk+PgplbmRvYmoKeHJlZgowIDEwCjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMDM2MCAwMDAwMCBuIAowMDAwMDAwNjM2IDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDcyNCAwMDAwMCBuIAowMDAwMDAwNjAxIDAwMDAwIG4gCjAwMDAwMDA3ODcgMDAwMDAgbiAKMDAwMDAwMDg0MSAwMDAwMCBuIAowMDAwMDAwODczIDAwMDAwIG4gCjAwMDAwMDA5NzYgMDAwMDAgbiAKdHJhaWxlcgo8PC9JbmZvIDkgMCBSL0lEIFs8YzMyYTdmODY5ZDI0MzY2Mjk3MDIxMDkxYTAyNzFmZDE+PDhjNGI1ZWQ4OWNiYTk0NTMwYmRkMGNmZmJlYzNhN2QxPl0vUm9vdCA4IDAgUi9TaXplIDEwPj4Kc3RhcnR4cmVmCjExMzEKJSVFT0YK"
    },
    {
      "pdfFilename": "s3-report-e56cd3e0-4b68-456f-ad2f-9e060473f8f9.pdf",
      "pdfBase64String": "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDI3OD4+c3RyZWFtCnicrZJNSwMxEIbv8yvmWKGsk+zmY6+KCoKiNhcRD0sba223W9eV4r83We22Fo0tDSHJkLyTZ+Ylr3BiIJWoSaIZwZmBW+B46U8Zkht+1RlHU8LxOUNGaJ6gd2RevHYtIRyWm0ncCZVWiVarTC59JrX39Rh6p5PmA2u7qOrGv0Y4/uXFh0e3j9qK/mblDiVEomVXJP+Jui5KeyCDi/8gN9XifVY0k2oeoZ00S9SW5xvt2CXeV/U0RktBEON9IvJzJxQlApdO5iOJq3l30WlSx2NOVIJQ+juewWB3W1gaqPaqmA+f7Vtj6xjGBFEsui+M1r608T6+yJwHih001TTG9w9TGE/7mZCxDJE67wz5ircM+QRaVfRfCmVuZHN0cmVhbQplbmRvYmoKMSAwIG9iago8PC9UYWJzL1MvR3JvdXA8PC9TL1RyYW5zcGFyZW5jeS9UeXBlL0dyb3VwL0NTL0RldmljZVJHQj4+L0NvbnRlbnRzIDMgMCBSL1R5cGUvUGFnZS9SZXNvdXJjZXM8PC9Db2xvclNwYWNlPDwvQ1MvRGV2aWNlUkdCPj4vUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUMgL0ltYWdlSV0vRm9udDw8L0YxIDIgMCBSPj4+Pi9QYXJlbnQgNCAwIFIvTWVkaWFCb3hbMCAwIDU5NSA4NDJdPj4KZW5kb2JqCjUgMCBvYmoKWzEgMCBSL1hZWiAwIDg1MiAwXQplbmRvYmoKMiAwIG9iago8PC9TdWJ0eXBlL1R5cGUxL1R5cGUvRm9udC9CYXNlRm9udC9IZWx2ZXRpY2EvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nPj4KZW5kb2JqCjQgMCBvYmoKPDwvS2lkc1sxIDAgUl0vVHlwZS9QYWdlcy9Db3VudCAxL0lUWFQoMi4xLjcpPj4KZW5kb2JqCjYgMCBvYmoKPDwvTmFtZXNbKEpSX1BBR0VfQU5DSE9SXzBfMSkgNSAwIFJdPj4KZW5kb2JqCjcgMCBvYmoKPDwvRGVzdHMgNiAwIFI+PgplbmRvYmoKOCAwIG9iago8PC9OYW1lcyA3IDAgUi9UeXBlL0NhdGFsb2cvUGFnZXMgNCAwIFIvVmlld2VyUHJlZmVyZW5jZXM8PC9QcmludFNjYWxpbmcvQXBwRGVmYXVsdD4+Pj4KZW5kb2JqCjkgMCBvYmoKPDwvTW9kRGF0ZShEOjIwMTYwMzMxMjE1NjM2WikvQ3JlYXRvcihKYXNwZXJSZXBvcnRzIExpYnJhcnkgdmVyc2lvbiA2LjIuMCkvQ3JlYXRpb25EYXRlKEQ6MjAxNjAzMzEyMTU2MzZaKS9Qcm9kdWNlcihpVGV4dCAyLjEuNyBieSAxVDNYVCk+PgplbmRvYmoKeHJlZgowIDEwCjAwMDAwMDAwMDAgNjU1MzUgZiAKMDAwMDAwMDM2MCAwMDAwMCBuIAowMDAwMDAwNjM2IDAwMDAwIG4gCjAwMDAwMDAwMTUgMDAwMDAgbiAKMDAwMDAwMDcyNCAwMDAwMCBuIAowMDAwMDAwNjAxIDAwMDAwIG4gCjAwMDAwMDA3ODcgMDAwMDAgbiAKMDAwMDAwMDg0MSAwMDAwMCBuIAowMDAwMDAwODczIDAwMDAwIG4gCjAwMDAwMDA5NzYgMDAwMDAgbiAKdHJhaWxlcgo8PC9JbmZvIDkgMCBSL0lEIFs8ODgwOTg2NWQ1MTAzZjMyMWFlY2Q5NzMwNjk0ODBhZGY+PDc2MGU5ZWRjMTk2ODcwNTg4MTA1MmFiOTNmYjJhM2I0Pl0vUm9vdCA4IDAgUi9TaXplIDEwPj4Kc3RhcnR4cmVmCjExMzEKJSVFT0YK"
    }
  ]
}

API Gateway Model
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "JasperReportRequestModel",
  "type": "object",
    "properties": {
      "xmlRequestData": {
        "type": "array",
        "items": {
          "type": "object",
          "properties": {
            "xmlRequest": { "type": "string" }
          }
        }
      }
    }
}
---------
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "PhotosInputModel",
  "type": "object",
  "properties": {
    "xmlRequestData": {
      "type": "object",
      "properties": {
        "page": { "type": "integer" },
        "pages": { "type": "string" },
        "perpage": { "type": "integer" },
        "total": { "type": "string" },
        "photo": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "id": { "type": "string" },
            }
          }
        }
      }
    }
  }
}

Readme.md
----------
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "PhotosInputModel",
  "type": "object",
      "properties": {
        "photo": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "id": { "type": "string" },
            }
          }
        }
  }
}
--------------
{
  "photos": {
    "photo": [
      {
        "id": "12345678901",
      },
      {
        "id": "23456789012",
      }
    ]
  }
}
----------
{
  "xmlRequestData": [
    { 
      "xmlRequest" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
    },
    {
      "xmlRequest" : "<?xml version=\"1.0\" encoding=\"UTF-8\"?><report type=\"s3\" xml-select-expression=\"/report/city\" jrxml-location=\"jasperreports-jrxml\" jrxml=\"report.jrxml\" pdf-location=\"jasperreports-generated-pdf\" pdf=\"s3-report.pdf\"><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
    }
  ]
}

----------
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "FeedbackModel",
  "type": "array",
  "items": {
    "type": "object",
    "properties": {
      "feedback": { "type": "integer", "minimum": 1, "maximum": 5 }
    }
  }
}
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "GroceryStoreOutputModel",
  "type": "object",
  "properties": {
    "choices": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "kind": { "type": "string" },
          "suggestedPrice": { "type": "string" },
          "available": { "type": "integer" }
        }
      }
    }
  }
}
I
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "GroceryStoreInputModel",
  "type": "object",
  "properties": {
    "department": { "type": "string" },
    "categories": {
      "type": "array",
      "items": { "type": "string" }
    },
    "bins": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "category": { "type": "string" },
          "type": { "type": "string" },
          "price": { "type": "number" },
          "unit": { "type": "string" },
          "quantity": { "type": "integer" }
        }
      }
    }
  }
}