package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
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
	 * @param semanticClassification The Semantic Classification of the Noun.
	 * @param countability The Countability of the Noun.
	 */
	public CommonNoun(
		long token,
		SemanticClassification semanticClassification,
		Countability countability
	) {
		super(
			token,
			new NounCategories(
				LexicalCategory.COMMON,
				semanticClassification,
				countability
			)
		);
	}

	/**
	 * Constructs a Lexical Noun from a token and the language's Grammatical Gender for this Noun.
	 * @param token The token that represents the Lexical Noun.
	 * @return The request Lexical Noun wrapped in an {@link Optional} if found, otherwise an empty {@link Optional}.
	 * @throws ArgumentNullException Thrown if genderProvider is null.
	 * @throws ArgumentMembershipException Thrown if the token does not represent a Lexical Noun.
	 */
	public static Optional<CommonNoun> fromToken(long token)
			throws ArgumentNullException, ArgumentMembershipException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Nouns.Common.all(), "Lexical Tokens");
		return Tokens.Nouns.Common.get(token);
	}
	
	@Override
	protected Object clone() {
		return
			new CommonNoun(
				this.getToken(),
				this.getSemanticClassification(),
				this.getCountability()
			);
	}

	@Override
	public Optional<GrammaticalGender> getGender(Supplier<GrammaticalGender> genderSupplier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(genderSupplier, "genderSupplier");
		return Optional.ofNullable(genderSupplier.get());
	}
}