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
		this.distributive = Optional.empty();
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
	
	/**
	 * Gets the {@link DeterminerDistributive} of the {@link DeterminerPhrase}.
	 * @return The {@link DeterminerDistributive} wrapped in an {@link Optional}, if present, otherwise an empty {@link Optional}.
	 */
	public Optional<DeterminerDistributive> getDistributive() {
		return this.distributive;
	}
	
	/**
	 * Sets the {@link DeterminerDistributive} of the {@link DeterminerPhrase}.
	 * @param distributive The {@link DeterminerDistributive} to set in the {@link DeterminerPhrase}.
	 */
	public void setDistributive(DeterminerDistributive distributive) {
		this.distributive = Optional.ofNullable(distributive);
	}
	
	/**
	 * Gets the {@link PronounPossessive} of the determiner phrase.
	 * @return The {@link PronounPossessive} of a determiner phrase (such as "my", "your", etc.) wrapped in an {@link Optional}, if present, otherwise an empty {@link Optional}.
	 */
	public Optional<PronounPossessive> getPossessive() {
		return this.possessive;
	}
	
	/**
	 * Sets the {@link PronounPossessive} of the {@link DeterminerPhrase}.
	 * @param possessive The {@link PronounPossessive} of the {@link DeterminerPhrase}.
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