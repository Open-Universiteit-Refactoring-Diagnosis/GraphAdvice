package nl.ou.refactoring.advice.nlp.grammar.determiners;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

/**
 * A key for a Possessive Pronoun's declension.
 * @param person The grammatical person of the Possessive Pronoun.
 * @param gender The grammatical gender of the Possessive Pronoun.
 * @param number The grammatical number of the Possessive Pronoun.
 * @param genderModifier The grammatical gender of the modifier of the Possessive Pronoun.
 * @param numberModifier The grammatical number of the modifier of the Possessive Pronoun.
 * @param register The register of the Sentence.
 */
public final record PronounPossessiveKey
(
	GrammaticalPerson person,
	GrammaticalGender gender,
	GrammaticalNumber number,
	GrammaticalGender genderModifier,
	GrammaticalNumber numberModifier,
	GrammaticalRegister register
) { }
