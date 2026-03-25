package nl.ou.refactoring.advice.nlp.grammar.nouns;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;

/**
 * The properties of a Common Noun, that are derived by the language the Noun is from.
 * @param gender The grammatical gender of the Noun.
 */
public final record CommonNounProperties
(
	GrammaticalGender gender
) { }