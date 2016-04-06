package uk.co.keithsjohnson.xmltojsonconverter.lambda.api;

public class JSONResponse {
	private String jsonResponse;

	public JSONResponse() {
	}

	public JSONResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public String getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	@Override
	public String toString() {
		return "JSONResponse [jsonResponse=" + jsonResponse + "]";
	}
}
