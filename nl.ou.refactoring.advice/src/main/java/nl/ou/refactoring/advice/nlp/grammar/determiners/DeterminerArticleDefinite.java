package nl.ou.refactoring.advice.nlp.grammar.determiners;

import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a definite article in a natural language grammar.
 */
public final class DeterminerArticleDefinite extends DeterminerArticle {
	/**
	 * The token for a definite article.
	 */
	public static final long TOKEN = Tokens.Determiners.Articles.THE;
	
	/**
	 * Initialises a new instance of {@link DeterminerArticleDefinite}.
	 */
	public DeterminerArticleDefinite() {
		super(TOKEN);
	}
}