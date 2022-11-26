package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HeaderConfig;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

abstract class RestClient {
	
	protected final String baseUri;
	
	public RestClient(String baseUri) {
		this.baseUri = baseUri;
	}
	
	public RequestSpecification generateRequestSpecification() {
		return new RequestSpecBuilder()
				.setBaseUri(baseUri)
				.setConfig(
						RestAssured.config()
								.logConfig(
										LogConfig.logConfig()
												.enableLoggingOfRequestAndResponseIfValidationFails()
								)
								.headerConfig(
										HeaderConfig.headerConfig().overwriteHeadersWithName("Content-Type", "Accept")
								)
								.encoderConfig(
										EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
								)
				)
				.addFilters(
						List.of(
								new AllureRestAssured(),
								new ResponseLoggingFilter()
						)
				)
				.build();
	}
	
	private Response sendRequest(Method method, RequestSpecification requestSpecification) {
		return RestAssured.given().spec(requestSpecification).log().all().request(method);
	}
	
	public Response request(Method method, RequestSpecification requestSpecification, Integer expectedStatus) {
		Response response = sendRequest(method, requestSpecification);
		response.then().statusCode(expectedStatus);
		return StringUtils.isNotBlank(response.asString()) ? response : null;
	}
	
	public Response request(Method method, RequestSpecification requestSpecification) {
		Response response = sendRequest(method, requestSpecification);
		return StringUtils.isNotBlank(response.asString()) ? response : null;
	}
}
