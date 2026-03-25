package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
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
	public ProperNoun(long token, SemanticClassification semanticClassification) {
		super(
			token,
			new NounCategories(
				LexicalCategory.PROPER,
				semanticClassification,
				Countability.UNCOUNTABLE
			)
		);
	}

	@Override
	protected Object clone() {
		return
			new ProperNoun(
				this.getToken(),
				this.getSemanticClassification()
			);
	}

	@Override
	public Optional<GrammaticalGender> getGender(Supplier<GrammaticalGender> genderSupplier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(genderSupplier, "genderSupplier");
		return Optional.ofNullable(genderSupplier.get());
	}
	
	@Override
	public String toString() {
		return "Proper Noun " + this.getToken();
	}
}