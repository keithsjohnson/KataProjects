XML to JSON Converter AWS Lambda Simple Example
-----------------------------------------------

Status
------
This is work in progress - do not use.

Author
------
Keith Johnson

Source
------
See my KataProjects repo on GitHub:
https://github.com/keithsjohnson/KataProjects

in the uk.co.keithsjohnson.xmltojsonconverter.lambda project:
https://github.com/keithsjohnson/KataProjects/tree/master/uk.co.keithsjohnson.xmltojsonconverter.lambda

Introduction
------------

Warning this project is still in progress and only an example of what AWS Lambda can be used for.

This project is a simple example for using an AWS Lambda written in Java to convert XML to JSON.

Build Information
-----------------
The build uses Gradle and use the following command from project home directory:
gradlew clean build

Lambda Handler Information
--------------------------
Name: XmlToJSONConverterLamda
Handler: uk.co.keithsjohnson.xmltojsonconverter.lambda.XmlToJSONConverterLamda::handleXmlToJsonConverter
Role: <Pick a role that has read and write access to S3>
Runtime: Java 8
Memory: 512Mb
Timeout: 1 min
VPC: No VPC
Upload file: build/distributions/uk.co.keithsjohnson.xmltojsonconverter.lambda-1.0.zip

Test Data
---------
{
  "xmlRequest": "<report type='s3' xml-select-expression='/report/city' jrxml-location='jasperreports-jrxml' jrxml='report.jrxml' pdf-location='jasperreports-generated-pdf' pdf='s3-report.pdf'><city><name>New York</name><population>12000000</population></city><city><name>Manchester</name><population>1000000</population></city><city><name>Stoke</name><population>123456</population></city></report>"
}

