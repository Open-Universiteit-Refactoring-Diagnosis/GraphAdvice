package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;

/**
 * Represents a Verb Phrase in Natural Language grammar.
 */
public final class VerbPhrase extends Phrase {
	private final Verb verb;
	private Optional<PrepositionalPhrase> prepositionalPhrase = Optional.empty();
	private VerbConjugationKey conjugation = VerbConjugationKey.DEFAULT;
	
	/**
	 * Initialises a new instance of {@link VerbPhrase}.
	 * @param verbFactory Constructs the main verb of the verb phrase.
	 * @throws ArgumentNullException Thrown if verb is null.
	 */
	public VerbPhrase(Function<VerbPhrase, Verb> verbFactory)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(verbFactory, "verb");
		this.verb = verbFactory.apply(this);
	}
	
	/**
	 * Gets the Prepositional Phrase, if any.
	 * @return The Prepositional Phrase that applies to the main Verb.
	 */
	public Optional<PrepositionalPhrase> getPrepositionalPhrase() {
		return this.prepositionalPhrase;
	}
	
	/**
	 * Sets the Prepositional Phrase.
	 * @param prepositionalPhrase The Prepositional Phrase that applies to the main Verb.
	 */
	public void setPrepositionalPhrase(PrepositionalPhrase prepositionalPhrase) {
		this.prepositionalPhrase = Optional.ofNullable(prepositionalPhrase);
	}
	
	/**
	 * Gets the conjugation of the {@link VerbPhrase}.
	 * @return The {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 */
	public VerbConjugationKey getConjugation() {
		return this.conjugation;
	}
	
	/**
	 * Sets the conjugation of the {@link VerbPhrase}.
	 * @param conjugation The {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 * @throws ArgumentNullException Thrown if conjugation is null.
	 */
	public void setConjugation(VerbConjugationKey conjugation)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(conjugation, "conjugation");
		this.conjugation = conjugation;
	}
	
	/**
	 * Gets the verb of the verb phrase.
	 * @return The verb of the verb phrase.
	 */
	public Verb getVerb() {
		return this.verb;
	}
}