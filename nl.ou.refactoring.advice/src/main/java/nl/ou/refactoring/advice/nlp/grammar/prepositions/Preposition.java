package nl.ou.refactoring.advice.nlp.grammar.prepositions;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
import nl.ou.refactoring.advice.nlp.grammar.SyntaxElement;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Preposition in Natural Language.
 */
public final class Preposition implements SyntaxElement {
	public final long token;
	
	/**
	 * Initialises a new instance of {@link Preposition}.
	 * @throws ArgumentMembershipException Thrown if the token is not linked to a Preposition.
	 */
	public Preposition(long token)
			throws ArgumentMembershipException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Prepositions.all(), "Prepositions");
		this.token = token;
	}
	
	/**
	 * Gets the token that represents the Preposition.
	 * @return The token that represents the Preposition.
	 */
	public long getToken() {
		return this.token;
	}
	
	/**
	 * Constructs a Preposition from the specified token.
	 * @param token The token that should represent a Preposition.
	 * @return The constructed Preposition.
	 * @throws ArgumentMembershipException Thrown if token does not represent a Preposition.
	 */
	public static Optional<Preposition> fromToken(long token) {
		return Tokens.Prepositions.get(token);
	}
}