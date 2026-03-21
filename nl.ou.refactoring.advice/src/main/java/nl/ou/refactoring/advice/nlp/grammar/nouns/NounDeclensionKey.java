package nl.ou.refactoring.advice.nlp.grammar.nouns;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;

/**
 * A key for a Noun's declension.
 * @param number The grammatical number of the Noun.
 */
public record NounDeclensionKey
(
	GrammaticalNumber number
) { }