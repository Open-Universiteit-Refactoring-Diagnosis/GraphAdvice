package nl.ou.refactoring.advice.nlp.grammar.determiners;

/**
 * Represents an article in a language's grammar.
 */
public abstract class DeterminerArticle implements DeterminerWithToken {
	private final long token;
	
	/**
	 * Initialises a new instance of {@link DeterminerArticle}.
	 */
	protected DeterminerArticle(long token) {
		this.token = token;
	}
	
	/**
	 * Gets the token of the Article.
	 */
	@Override
	public final long getToken() {
		return this.token;
	}
}