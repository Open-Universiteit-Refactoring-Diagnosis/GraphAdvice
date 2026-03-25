package nl.ou.refactoring.advice.nlp.grammar.adjectives;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;

/**
 * A key for the declension of an adjective in a Natural Language grammar.
 * @param gender The grammatical gender the adjective needs to agree to.
 * @param number The grammatical number the adjective needs to agree to.
 */
public final record AdjectiveDeclensionKey
(
	GrammaticalGender gender,
	GrammaticalNumber number
) { }