package nl.ou.refactoring.advice.nlp.grammar.determiners;

/**
 * Represents a Determiner in a Natural Language grammar that has a token.
 */
public interface DeterminerWithToken extends Determiner {
	/**
	 * Gets the token of the determiner.
	 * @return The token of the determiner.
	 */
	long getToken();
}