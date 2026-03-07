package nl.ou.refactoring.advice.contracts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class ArgumentPatternExceptionTests {
	@Test
	@DisplayName("Should return localised message")
	public void getLocalizedMesageTest() {
		// Arrange
		final var parameterName = "testParameter";
		final var pattern = Pattern.compile("^hello-world$");
		final var exception = new ArgumentPatternException(pattern, parameterName);
		
		// Act
		Locale.setDefault(Locale.of("nl", "NL"));
		final var exceptionMessageDutch = exception.getLocalizedMessage();
		Locale.setDefault(Locale.of("en", "GB"));
		final var exceptionMessageEnglish = exception.getLocalizedMessage();
		
		// Assert
		assertEquals("`testParameter` komt niet overeen met het patroon `^hello-world$`.", exceptionMessageDutch);
		assertEquals("`testParameter` does not match the pattern `^hello-world$`.", exceptionMessageEnglish);
	}
}