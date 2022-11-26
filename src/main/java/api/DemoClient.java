package api;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class DemoClient extends RestClient {
	private static final String baseUri = "http://localhost";
	
	public DemoClient() {
		super(baseUri);
	}
	
	@Step("Sign user in")
	public Document signIn(String email, String password, Integer expectedStatus) {
		Map<String, String> body = new HashMap<>();
		body.put("email", email);
		body.put("password", password);
		RequestSpecification reqSpec = generateRequestSpecification()
				.port(8080)
				.basePath("/signin")
				.formParams(body);
		
		Response response = request(Method.POST, reqSpec, expectedStatus);
		return Jsoup.parse(response.body().asString());
	}
}
