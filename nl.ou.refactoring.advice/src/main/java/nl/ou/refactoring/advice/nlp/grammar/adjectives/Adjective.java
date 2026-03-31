package nl.ou.refactoring.advice.nlp.grammar.adjectives;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.SyntaxElement;

/**
 * Represents an Adjective in Natural Language.
 */
public final class Adjective implements SyntaxElement {
	private final long token;
	
	/**
	 * Initialises a new instance of {@link Adjective}.
	 * @param token The token of the {@link Adjective}.
	 * @throws ArgumentNullException Thrown if token is null.
	 * @throws ArgumentEmptyException Thrown if token is empty or contains only white spaces.
	 */
	public Adjective(long token)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireGreaterThanOrEqual(0, token, "token");
		this.token = token;
	}
	
	/**
	 * Gets the token that indicates the {@link Adjective} in any language.
	 * @return The token that indicates the {@link Adjective} in any language.
	 */
	public long getToken() {
		return this.token;
	}
}