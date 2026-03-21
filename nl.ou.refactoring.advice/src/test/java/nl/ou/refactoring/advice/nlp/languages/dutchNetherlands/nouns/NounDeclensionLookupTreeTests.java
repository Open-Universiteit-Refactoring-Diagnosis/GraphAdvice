package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands.nouns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;

public class NounDeclensionLookupTreeTests {
	@DisplayName("Should look up the correct declension of a noun")
	@ParameterizedTest
	@MethodSource("lookupTestParameters")
	public void lookupTests(String stem, NounDeclensionKey key, String expected) {
		final var lookupTree = new NounDeclensionLookupTree(stem);
		final var actualOptional = lookupTree.lookup(key);
		assertTrue(actualOptional.isPresent());
		assertEquals(expected, actualOptional.get());
	}
	
	public static Stream<Arguments> lookupTestParameters() {
		return
			Stream.of(
				Arguments.of("water", new NounDeclensionKey(GrammaticalNumber.SINGULAR), "water"),
				Arguments.of("water", new NounDeclensionKey(GrammaticalNumber.PLURAL), "wateren")
			);
	}
}
