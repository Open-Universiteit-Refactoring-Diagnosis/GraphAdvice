package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * A Common Noun (non-specific people, places, things, or ideas).
 */
public final class CommonNoun extends Noun {
	/**
	 * Initialises a new instance of {@link CommonNoun}.
	 * @param token The token of the Lexical Noun.
	 * @param lexicalCategory The Lexical Category of the Noun.
	 * @param semanticClassification The Semantic Classification of the Noun.
	 * @param countability The Countability of the Noun.
	 * @param genderSupplier Supplies the Grammatical Gender of the Noun. Each language may designate a different grammatical gender to a Common Noun.
	 */
	public CommonNoun(
		long token,
		SemanticClassification semanticClassification,
		Countability countability,
		Supplier<GrammaticalGender> genderSupplier
	) {
		super(token, LexicalCategory.COMMON, semanticClassification, countability, genderSupplier);
	}

	/**
	 * Constructs a Lexical Noun from a token and the language's Grammatical Gender for this Noun.
	 * @param token The token that represents the Lexical Noun.
	 * @param genderSuppliers Provides the language's Grammatical Gender for this Noun.
	 * @return The request Lexical Noun wrapped in an {@link Optional} if found, otherwise an empty {@link Optional}.
	 * @throws ArgumentNullException Thrown if genderProvider is null.
	 * @throws ArgumentMembershipException Thrown if the token does not represent a Lexical Noun.
	 */
	public static Optional<CommonNoun> fromToken(
		long token,
		Function<Long, Optional<GrammaticalGender>> genderSuppliers
	) throws ArgumentNullException, ArgumentMembershipException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Nouns.Common.all(), "Lexical Tokens");
		ArgumentGuard.requireNotNull(genderSuppliers, "genderProvider");
		return
			genderSuppliers
				.apply(token)
				.map((gender) -> Tokens.Nouns.Common.get(token, gender))
				.orElse(Optional.empty());
	}
	
	@Override
	protected Object clone() {
		return
			new CommonNoun(
				this.getToken(),
				this.getSemanticClassification(),
				this.getCountability(),
				this.getGenderSupplier()
			);
	}
}
