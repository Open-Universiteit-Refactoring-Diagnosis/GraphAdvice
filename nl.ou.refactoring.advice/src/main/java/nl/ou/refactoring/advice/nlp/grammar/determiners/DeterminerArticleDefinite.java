package nl.ou.refactoring.advice.nlp.grammar.determiners;

import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a definite article in a natural language grammar.
 */
public final class DeterminerArticleDefinite extends DeterminerArticle {
	/**
	 * The token for a definite article.
	 */
	public static final long TOKEN = Tokens.Determiners.THE;
	
	/**
	 * Initialises a new instance of {@link DetermineArticleDefinite}.
	 */
	public DeterminerArticleDefinite() { }
	
	@Override
	public long getToken() {
		return TOKEN;
	}
}