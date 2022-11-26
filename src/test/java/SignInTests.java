import api.DemoClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import us.codecraft.xsoup.Xsoup;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class SignInTests {
	private final DemoClient client = new DemoClient();
	
	@Test
	@Epic("Система аутентификации")
	@Feature("Авторизация")
	@Story("Успешная авторизация")
	void correctAuthorization() {
		Document signInResult = client.signIn("USER@test.ci", "password1", 200);
		String result = Xsoup.compile("//div/@class=card-header/h4/text()").evaluate(signInResult).get();
		
		assertThat(result).as("Проверяем результат авторизации").isEqualTo("Выполнен успешный вход.");
	}
	
	@ParameterizedTest(name = "Авторизация с реквизитами: {0}")
	@MethodSource("negativeData")
	@Epic("Система аутентификации")
	@Feature("Авторизация")
	@Story("Неуспешная авторизация")
	void incorrectAuthorization(String testName, String email, String password, Integer expectedStatus, String expectedResult) {
		Document signInResult = client.signIn(email, password, expectedStatus);
		String result = Xsoup.compile("//div/@class=card-header/h4/text()").evaluate(signInResult).get();
		
		assertThat(result).as("Проверяем результат авторизации").isEqualTo(expectedResult);
	}
	
	private static Stream<Arguments> negativeData() {
		return Stream.of(
				Arguments.arguments("неправильные учетные данные", "USER@test.ci", "password5", 401, "Невозможно войти в систему."),
				Arguments.arguments("пустые учетные данные", "", "", 400, "Необходимо указать пароль."),
				Arguments.arguments("учетные данные равны null", null, null, 400, "Необходимо указать пароль.")
		);
	}
}
