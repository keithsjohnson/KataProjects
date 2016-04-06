package uk.co.keithsjohnson.xmltojsonconverter.lambda;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestLambdaLogger implements LambdaLogger {
	public void log(String string) {
		System.out.println(string);
	}
}
