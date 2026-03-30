package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;

public class NLPLanguageDutchNetherlandsTests {
	@DisplayName("Should generate correct sentences from a natural language syntax tree")
	@ParameterizedTest
	@ArgumentsSource(NLPLanguageDutchNetherlandsVisitTestsArgumentsProvider.class)
	public void visitTests(Sentence sentence, NLPResult expected) {
		final var language = NLPLanguageDutchNetherlands.INSTANCE;
		final var actual = language.visit(sentence);
		assertEquals(expected, actual);
	}
}
