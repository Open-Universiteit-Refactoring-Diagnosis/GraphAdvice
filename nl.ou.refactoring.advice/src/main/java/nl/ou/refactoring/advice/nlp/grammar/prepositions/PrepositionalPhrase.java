package nl.ou.refactoring.advice.nlp.grammar.prepositions;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;

/**
 * Represents a Prepositional Phrase in Natural Language.
 */
public class PrepositionalPhrase extends Phrase {
	private final Preposition preposition;
	private final NounPhrase nounPhrase;
	
	/**
	 * Initialises a new instance of {@link PrepositionalPhrase}.
	 * @param preposition The main {@link Preposition} of the phrase.
	 * @param nounPhrase A Noun Phrase that is linked to the Preposition.
	 * @throws ArgumentNullException Thrown if preposition or nounPhrase is null.
	 */
	public PrepositionalPhrase(Preposition preposition, NounPhrase nounPhrase)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(preposition, "preposition");
		ArgumentGuard.requireNotNull(nounPhrase, "nounPhrase");
		this.preposition = preposition;
		this.nounPhrase = nounPhrase;
	}
	
	/**
	 * Gets the Noun Phrase of the Prepositional Phrase.
	 * @return The Noun Phrase of the Prepositional Phrase.
	 */
	public NounPhrase getNounPhrase() {
		return this.nounPhrase;
	}
	
	/**
	 * Gets the Preposition of the Prepositional Phrase.
	 * @return The Preposition of the Prepositional Phrase.
	 */
	public Preposition getPreposition() {
		return this.preposition;
	}
}