package nl.ou.refactoring.advice.nlp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class NLPTransformerTests {

	@DisplayName("Should capitalise the first letter of a String")
	@ParameterizedTest
	@MethodSource("capitaliseFirstLetterTestArguments")
	public void capitaliseFirstLetterTests(String original, String expected) {
		final var actual = NLPTransformer.capitaliseFirstLetter(original);
		assertEquals(expected, actual);
	}
	
	public static Stream<? extends Arguments> capitaliseFirstLetterTestArguments() {
		return
			Stream.of(
				Arguments.of(null, null),
				Arguments.of("", ""),
				Arguments.of("a", "A"),
				Arguments.of("A", "A"),
				Arguments.of("abc", "Abc")
			);
	}
}
