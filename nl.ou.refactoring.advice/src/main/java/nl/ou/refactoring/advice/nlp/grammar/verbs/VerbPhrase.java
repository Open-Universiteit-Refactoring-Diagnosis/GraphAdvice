package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;

/**
 * Represents a Verb Phrase in Natural Language grammar.
 */
public final class VerbPhrase extends Phrase {
	private final Verb verb;
	private Optional<AuxiliaryVerb> auxiliaryVerb;
	private Optional<PrepositionalPhrase> prepositionalPhrase;
	
	/**
	 * Initialises a new instance of {@link VerbPhrase}.
	 * @param verb The verb of the verb phrase.
	 * @throws ArgumentNullException Thrown if verb is null.
	 */
	public VerbPhrase(Verb verb) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(verb, "verb");
		this.verb = verb;
	}
	
	/**
	 * Gets the Auxiliary Verb, if any.
	 * @return The Auxiliary Verb, if present, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<AuxiliaryVerb> getAuxiliaryVerb() {
		return this.auxiliaryVerb;
	}
	
	/**
	 * Sets the Auxiliary Verb.
	 * @param auxiliaryVerb The Auxiliary Verb that supplements the main Verb.
	 */
	public void setAuxiliaryVerb(AuxiliaryVerb auxiliaryVerb) {
		this.auxiliaryVerb = Optional.ofNullable(auxiliaryVerb);
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
	 * Gets the verb of the verb phrase.
	 * @return The verb of the verb phrase.
	 */
	public Verb getVerb() {
		return this.verb;
	}
}