package nl.ou.refactoring.advice.nlp.grammar.determiners;

import java.util.Optional;

/**
 * Represents a determiner phrase in a Natural Language grammar.
 */
public final class DeterminerPhrase implements Determiner {
	private Optional<DeterminerArticle> article;
	private Optional<DeterminerDistributive> distributive;
	private Optional<PronounPossessive> possessive; 
	private Optional<DeterminerQuantifier> quantifier;
	
	/**
	 * Initialises a new instance of {@link DeterminerPhrase}.
	 */
	public DeterminerPhrase() {
		this.article = Optional.empty();
		this.possessive = Optional.empty();
		this.quantifier = Optional.empty();
	}
	
	/**
	 * Gets the article part of the determiner phrase.
	 * @return The article part of the determiner phrase.
	 */
	public Optional<DeterminerArticle> getArticle() {
		return this.article;
	}
	
	/**
	 * Sets a new value for the article part of the determiner phrase.
	 * @param article The article part of the determiner phrase.
	 */
	public void setArticle(DeterminerArticle article) {
		this.article = Optional.ofNullable(article);
	}
	
	public Optional<DeterminerDistributive> getDistributive() {
		return this.distributive;
	}
	
	public void setDistributive(DeterminerDistributive distributive) {
		this.distributive = Optional.ofNullable(distributive);
	}
	
	/**
	 * Gets the possessive pronoun of the determiner phrase.
	 * @return The possessive pronoun of a determiner phrase (such as "my", "your", etc.).
	 */
	public Optional<PronounPossessive> getPossessive() {
		return this.possessive;
	}
	
	/**
	 * Sets the possessive pronoun of the determiner phrase.
	 * @param possessive The possessive pronoun of the determiner phrase.
	 */
	public void setPossessive(PronounPossessive possessive) {
		this.possessive = Optional.ofNullable(possessive);
	}
	
	/**
	 * Gets the quantifier part of the determiner phrase.
	 * @return The quantifier part of the determiner phrase.
	 */
	public Optional<DeterminerQuantifier> getQuantifier() {
		return this.quantifier;
	}
	
	/**
	 * Sets the quantifier part of the determiner phrase.
	 * @param quantifier The quantifier to set.
	 */
	public void setQuantifier(DeterminerQuantifier quantifier) {
		this.quantifier = Optional.ofNullable(quantifier);
	}
}