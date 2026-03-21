package nl.ou.refactoring.advice.nlp.grammar.nouns;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;

/**
 * Represents a Proper Noun (names of people, places or organisations).
 */
public final class ProperNoun extends Noun {
	/**
	 * Initialises a new instance of {@link ProperNoun}.
	 * @param token The token that represents the Proper Noun.
	 * @param semanticClassification The Semantic Classification of the Proper Noun.
	 * @param gender The Grammatical Gender of the Proper Noun.
	 */
	public ProperNoun(long token, SemanticClassification semanticClassification, GrammaticalGender gender) {
		super(token, LexicalCategory.PROPER, semanticClassification, Countability.UNCOUNTABLE, () -> gender);
	}

	@Override
	protected Object clone() {
		return new ProperNoun(this.getToken(), this.getSemanticClassification(), this.getGender());
	}
}