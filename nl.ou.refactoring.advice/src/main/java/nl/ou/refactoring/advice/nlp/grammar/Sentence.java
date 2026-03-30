package nl.ou.refactoring.advice.nlp.grammar;

import java.util.Optional;

import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;

/**
 * Represents a Sentence in Natural Language.
 */
public final class Sentence implements SyntaxElement {
	private Optional<NounPhrase> nounPhrase;
	private Optional<PrepositionalPhrase> prepositionalPhrase;
	private Optional<VerbPhrase> verbPhrase;
	
	/**
	 * Initialises a new instance of {@link Sentence}.
	 */
	public Sentence() {
		this.nounPhrase = Optional.empty();
		this.prepositionalPhrase = Optional.empty();
		this.verbPhrase = Optional.empty();
	}
	
	/**
	 * Gets the Noun Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @return The Noun Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<NounPhrase> getNounPhrase() {
		return this.nounPhrase;
	}
	
	/**
	 * Sets the Noun Phrase of the Sentence.
	 * @param nounPhrase The Noun Phrase of the Sentence.
	 */
	public void setNounPhrase(NounPhrase nounPhrase) {
		this.nounPhrase = Optional.ofNullable(nounPhrase);
	}
	
	/**
	 * Gets the {@link NounDeclensionKey} of the {@link NounPhrase}.
	 * @return The {@link NounDeclensionKey} of the {@link NounPhrase}.
	 */
	public Optional<NounDeclensionKey> getNounDeclensionKey() {
		return
			this
				.nounPhrase
				.map(nounPhrase -> nounPhrase.getDeclension());
	}
	
	/**
	 * Gets the Prepositional Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @return The Prepositional Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<PrepositionalPhrase> getPrepositionalPhrase() {
		return this.prepositionalPhrase;
	}
	
	/**
	 * 
	 * @param prepositionalPhrase
	 */
	public void setPrepositionalPhrase(PrepositionalPhrase prepositionalPhrase) {
		this.prepositionalPhrase = Optional.ofNullable(prepositionalPhrase);
	}
	
	/**
	 * Gets the Verb Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @return The Verb Phrase of the Sentence, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}..
	 */
	public Optional<VerbPhrase> getVerbPhrase() {
		return this.verbPhrase;
	}
	
	/**
	 * Sets the Verb Phrase of the Sentence.
	 * @param verbPhrase The Verb Phrase of the Sentence.
	 */
	public void setVerbPhrase(VerbPhrase verbPhrase) {
		this.verbPhrase = Optional.ofNullable(verbPhrase);
	}
	
	/**
	 * Gets the {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 * @return The {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 */
	public Optional<VerbConjugationKey> getConjugation() {
		return
			this
				.verbPhrase
				.map(verbPhrase -> verbPhrase.getConjugation());
	}
}