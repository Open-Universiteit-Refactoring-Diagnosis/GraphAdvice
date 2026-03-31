package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbModality;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTense;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;

public class VerbConjugationLookupTreeTests {
	@DisplayName("Should look up the correct conjugation of a verb")
	@ParameterizedTest
	@MethodSource("lookupDefaultTestParameters")
	public void lookupDefaultTests(String stem, VerbConjugationKey key, String expected) {
		final var lookupTree = NLPLanguageEnglishGreatBritain.VERB_CONJUGATION_DEFAULT.apply(stem);
		final var actualOptional = lookupTree.lookup(key);
		assertTrue(actualOptional.isPresent());
		assertEquals(expected, actualOptional.get());
	}
	
	public static Stream<Arguments> lookupDefaultTestParameters() {
		return
			Stream.of(
				Arguments.of(
					"add",
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.PLURAL,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"add"
				),
				Arguments.of(
					"add",
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"adds"
				),
				Arguments.of(
					"add",
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.PERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"added"
				)
			);
	}
	
	@DisplayName("Should look up the correct conjugation of the irregular verb 'to be'")
	@ParameterizedTest
	@MethodSource("lookupToBeTestParameters")
	public void lookupToBeTests(VerbConjugationKey key, String expected) {
		final var lookupTree = NLPLanguageEnglishGreatBritain.VERB_CONJUGATION_TO_BE;
		final var actualOptional = lookupTree.lookup(key);
		assertTrue(actualOptional.isPresent());
		assertEquals(expected, actualOptional.get());
	}
	
	public static Stream<Arguments> lookupToBeTestParameters() {
		return
			Stream.of(
				Arguments.of(
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.PLURAL,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"are"
				),
				Arguments.of(
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.IMPERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"is"
				),
				Arguments.of(
					new VerbConjugationKey(
						GrammaticalPerson.THIRD,
						GrammaticalNumber.SINGULAR,
						VerbAspect.PERFECTIVE,
						VerbModality.INDICATIVE,
						VerbTense.PRESENT,
						VerbVoice.ACTIVE,
						GrammaticalRegister.PLAIN
					),
					"been"
				)
			);
	}
}
