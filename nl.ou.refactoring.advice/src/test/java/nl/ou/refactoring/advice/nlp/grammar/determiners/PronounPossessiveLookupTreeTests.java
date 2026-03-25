package nl.ou.refactoring.advice.nlp.grammar.determiners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

public final class PronounPossessiveLookupTreeTests {
	@DisplayName("Should get the appropriate String value for a Possessive Pronoun")
	@ParameterizedTest
	@ArgumentsSource(PronounPossessiveLookupTreeTestsArgumentsProvider.class)
	public void getStringTests
	(
		GrammaticalPerson person,
		GrammaticalGender gender,
		GrammaticalNumber number,
		GrammaticalGender genderModifier,
		GrammaticalNumber numberModifier,
		GrammaticalRegister register,
		String expectedValue
	) {
		// Arrange
		final var pronounPossessive =
			new PronounPossessive
			(
				person,
				gender,
				number
			);
		final var pronounPossessiveKey =
			PronounPossessiveLookupTree
				.constructKey(
					pronounPossessive,
					genderModifier,
					numberModifier,
					register
				);
		
		// Act
		final var actualValueOptional =
			PronounPossessiveLookupTree
				.INSTANCE
				.lookup(pronounPossessiveKey);
		
		// Assert
		assertTrue(actualValueOptional.isPresent());
		final var actualValue = actualValueOptional.get();
		assertEquals(expectedValue, actualValue);
	}
}