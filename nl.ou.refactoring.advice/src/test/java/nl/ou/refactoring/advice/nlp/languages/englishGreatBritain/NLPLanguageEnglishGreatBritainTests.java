package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;

public final class NLPLanguageEnglishGreatBritainTests {
	@DisplayName("Should generate correct sentences from a natural language syntax tree")
	@ParameterizedTest
	@ArgumentsSource(NLPLanguageEnglishGreatBritainVisitTestsArgumentsProvider.class)
	public void visitTests(Sentence sentence, NLPResult expected) {
		final var language = NLPLanguageEnglishGreatBritain.INSTANCE;
		final var actual = language.visit(sentence);
		assertEquals(expected.getText(), actual.getText());
		assertEquals(expected.getReferences(), actual.getReferences());
		assertEquals(expected, actual);
	}
}