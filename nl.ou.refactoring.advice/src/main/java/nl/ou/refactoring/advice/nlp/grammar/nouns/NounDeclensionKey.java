package nl.ou.refactoring.advice.nlp.grammar.nouns;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

/**
 * A key for a Noun's declension.
 * @param number The grammatical number of the Noun.
 * @param register The grammatical register of the Noun.
 */
public record NounDeclensionKey
(
	GrammaticalNumber number,
	GrammaticalRegister register
) {
	/**
	 * The default declension for a {@link Noun}.
	 */
	public static final NounDeclensionKey DEFAULT =
		new NounDeclensionKey(
			GrammaticalNumber.SINGULAR,
			GrammaticalRegister.PLAIN
		);
}