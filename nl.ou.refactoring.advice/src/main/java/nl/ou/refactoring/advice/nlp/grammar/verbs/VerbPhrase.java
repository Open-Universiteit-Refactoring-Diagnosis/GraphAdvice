package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;

/**
 * Represents a Verb Phrase in Natural Language grammar.
 */
public final class VerbPhrase extends Phrase {
	private final Verb verb;
	private VerbConjugationKey conjugation = VerbConjugationKey.DEFAULT;
	private Optional<NounPhrase> nounPhrase = Optional.empty();
	private Optional<PrepositionalPhrase> prepositionalPhrase = Optional.empty();

	/**
	 * Initialises a new instance of {@link VerbPhrase}.
	 * 
	 * @param verbFactory Constructs the main verb of the verb phrase.
	 * @throws ArgumentNullException Thrown if verb is null.
	 */
	public VerbPhrase(Function<VerbPhrase, Verb> verbFactory) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(verbFactory, "verb");
		this.verb = verbFactory.apply(this);
	}

	/**
	 * Gets the conjugation of the {@link VerbPhrase}.
	 * 
	 * @return The {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 */
	public VerbConjugationKey getConjugation() {
		return this.conjugation;
	}

	/**
	 * Gets the {@link NounPhrase}, if present wrapped in an {@link Optional},
	 * otherwise an empty {@link Optional}.
	 * 
	 * @return The {@link NounPhrase}, if present wrapped in an {@link Optional},
	 *         otherwise an empty {@link Optional}.
	 */
	public Optional<NounPhrase> getNounPhrase() {
		return this.nounPhrase;
	}

	/**
	 * Gets the Prepositional Phrase, if any.
	 * 
	 * @return The Prepositional Phrase that applies to the main Verb.
	 */
	public Optional<PrepositionalPhrase> getPrepositionalPhrase() {
		return this.prepositionalPhrase;
	}

	/**
	 * Gets the verb of the verb phrase.
	 * 
	 * @return The verb of the verb phrase.
	 */
	public Verb getVerb() {
		return this.verb;
	}

	/**
	 * Sets the conjugation of the {@link VerbPhrase}.
	 * 
	 * @param conjugation The {@link VerbConjugationKey} of the {@link VerbPhrase}.
	 * @throws ArgumentNullException Thrown if conjugation is null.
	 */
	public void setConjugation(VerbConjugationKey conjugation) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(conjugation, "conjugation");
		this.conjugation = conjugation;
	}

	/**
	 * Sets the Noun Phrase of this Verb Phrase.
	 * 
	 * @param nounPhrase The Noun Phrase to set.
	 * @throws VerbIntransitiveCannotHaveNounPhraseException Thrown if the main Verb
	 *                                                       of this Verb Phrase is
	 *                                                       intransitive, so it
	 *                                                       cannot have a dependent
	 *                                                       Noun Phrase.
	 */
	public void setNounPhrase(NounPhrase nounPhrase) throws VerbIntransitiveCannotHaveNounPhraseException {
		if (this.verb.getTransitivity() == VerbTransitivity.INTRANSITIVE) {
			throw new VerbIntransitiveCannotHaveNounPhraseException(this.verb, nounPhrase);
		}
		this.nounPhrase = Optional.ofNullable(nounPhrase);
	}

	/**
	 * Sets the Prepositional Phrase.
	 * 
	 * @param prepositionalPhrase The Prepositional Phrase that applies to the main
	 *                            Verb.
	 */
	public void setPrepositionalPhrase(PrepositionalPhrase prepositionalPhrase) {
		this.prepositionalPhrase = Optional.ofNullable(prepositionalPhrase);
	}
}