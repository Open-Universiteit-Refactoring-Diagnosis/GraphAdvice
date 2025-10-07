package nl.ou.refactoring.advice.contracts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class ArgumentNullExceptionTests {
	@Test
	@DisplayName("Should return localised message")
	public void getLocalizedMessageTest() {
		// Arrange
		final var parameterName = "testParameter";
		final var exception = new ArgumentNullException(parameterName);
		
		// Act
		Locale.setDefault(Locale.of("nl", "NL"));
		final var exceptionMessageDutch = exception.getLocalizedMessage();
		Locale.setDefault(Locale.of("en", "GB"));
		final var exceptionMessageEnglish = exception.getLocalizedMessage();
		
		// Assert
		assertEquals("`testParameter` moet een waarde hebben.", exceptionMessageDutch);
		assertEquals("`testParameter` cannot be null.", exceptionMessageEnglish);
	}
}
