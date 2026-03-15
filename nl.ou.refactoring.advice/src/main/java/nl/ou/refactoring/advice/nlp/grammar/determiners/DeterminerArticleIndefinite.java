package nl.ou.refactoring.advice.nlp.grammar.determiners;

import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents an indefinite article in a natural language grammar.
 */
public final class DeterminerArticleIndefinite extends DeterminerArticle {
	/**
	 * A token for an indefinite article.
	 */
	public static final long TOKEN = Tokens.Determiners.A;

	/**
	 * Initialises a new instance of {@link DeterminerArticleIndefinite}.
	 */
	public DeterminerArticleIndefinite() { }
	
	@Override
	public long getToken() {
		return TOKEN;
	}
}