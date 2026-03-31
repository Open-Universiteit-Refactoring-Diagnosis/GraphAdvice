package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;

public class NounDeclensionLookupTreeTests {
	@DisplayName("Should look up the correct declension of a noun")
	@ParameterizedTest
	@MethodSource("lookupDefaultTestParameters")
	public void lookupDefaultTests(String stem, NounDeclensionKey key, String expected) {
		final var lookupTreeFactory = NLPLanguageDutchNetherlands.NOUN_DECLENSION_DEFAULT;
		final var lookupTree = lookupTreeFactory.apply(stem);
		final var actualOptional = lookupTree.lookup(key);
		assertTrue(actualOptional.isPresent());
		assertEquals(expected, actualOptional.get());
	}
	
	public static Stream<Arguments> lookupDefaultTestParameters() {
		return
			Stream.of(
				Arguments.of("land", new NounDeclensionKey(GrammaticalNumber.SINGULAR, GrammaticalRegister.PLAIN), "land"),
				Arguments.of("land", new NounDeclensionKey(GrammaticalNumber.PLURAL, GrammaticalRegister.PLAIN), "landen"),
				Arguments.of("schaap", new NounDeclensionKey(GrammaticalNumber.SINGULAR, GrammaticalRegister.PLAIN), "schaap"),
				Arguments.of("schaap", new NounDeclensionKey(GrammaticalNumber.PLURAL, GrammaticalRegister.PLAIN), "schapen"),
				Arguments.of("stap", new NounDeclensionKey(GrammaticalNumber.SINGULAR, GrammaticalRegister.PLAIN), "stap"),
				Arguments.of("stap", new NounDeclensionKey(GrammaticalNumber.PLURAL, GrammaticalRegister.PLAIN), "stappen")
			);
	}
}
