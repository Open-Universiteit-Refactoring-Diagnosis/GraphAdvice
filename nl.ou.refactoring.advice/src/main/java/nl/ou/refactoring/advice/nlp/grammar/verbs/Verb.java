package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;

/**
 * Represents a Verb in Natural Language grammar.
 */
public abstract class Verb {
	private final long token;
	private final VerbTransitivity transitivity;
	private final VerbPhrase phrase;
	private Optional<AuxiliaryVerb> auxiliaryVerb;
	
	/**
	 * Initialises a new instance of {@link Verb}.
	 * @param token The token that represents the verb.
	 * @param transitivity The transitivity of the verb.
	 * @param phrase The verb phrase that this verb belongs to.
	 * @throws ArgumentNullException Thrown if phrase is null.
	 */
	protected Verb(long token, VerbTransitivity transitivity, VerbPhrase phrase)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(phrase, "phrase");
		this.token = token;
		this.transitivity = transitivity;
		this.phrase = phrase;
		this.auxiliaryVerb = Optional.empty();
	}
	
	/**
	 * Gets the token that represents the verb.
	 * @return The token that represents the verb.
	 */
	public final long getToken() {
		return this.token;
	}
	
	/**
	 * Gets the {@link AuxiliaryVerb} for this verb.
	 * @return The {@link AuxiliaryVerb} for this verb.
	 */
	public final Optional<AuxiliaryVerb> getAuxiliaryVerb() {
		return this.auxiliaryVerb;
	}
	
	/**
	 * Gets a list of {@link AuxiliaryVerb}s.
	 * @return An unmodifiable list of {@link AuxiliaryVerb}s.
	 */
	public final List<AuxiliaryVerb> getAuxiliaryVerbs() {
		final var auxiliaryVerbs = new Stack<AuxiliaryVerb>();

		var auxiliaryVerbCurrent = this.auxiliaryVerb;
		while (auxiliaryVerbCurrent.isPresent()) {
			final var auxiliaryVerbCurrentUnwrapped = auxiliaryVerb.get();
			auxiliaryVerbs.push(auxiliaryVerbCurrentUnwrapped);
			auxiliaryVerbCurrent = auxiliaryVerbCurrentUnwrapped.getAuxiliaryVerb();
		}
		
		return Collections.unmodifiableList(auxiliaryVerbs);
	}
	
	/**
	 * Sets an {@link AuxiliaryVerb} for this {@link Verb}.
	 * @param auxiliaryVerb The {@link AuxiliaryVerb} to set for this {@link Verb}. If null, the Auxiliary Verb is cleared.
	 */
	void setAuxiliaryVerb(AuxiliaryVerb auxiliaryVerb) {
		this.auxiliaryVerb = Optional.ofNullable(auxiliaryVerb);
	}
	
	/**
	 * Gets the transitivity of the verb.
	 * @return The transitivity of the verb.
	 */
	public final VerbTransitivity getTransitivity() {
		return this.transitivity;
	}
	
	/**
	 * Gets the {@link VerbPhrase} of the {@link Verb}.
	 * @return The {@link VerbPhrase} of the {@link Verb}.
	 */
	public final VerbPhrase getPhrase() {
		return this.phrase;
	}
	
	/**
	 * Gets the aspect of the verb.
	 * @return The aspect of the verb.
	 */
	public abstract VerbAspect getAspect();
	
	/**
	 * Gets the modality of the verb.
	 * @return The modality of the verb.
	 */
	public abstract VerbModality getModality();
	
	/**
	 * Gets the grammatical number of the verb.
	 * @return The grammatical number of the verb.
	 */
	public abstract GrammaticalNumber getNumber();
	
	/**
	 * Gets the grammatical register of the verb.
	 * @return The grammatical register of the verb.
	 */
	public abstract GrammaticalRegister getRegister();
	
	/**
	 * Gets the grammatical person of the verb.
	 * @return The person of the verb.
	 */
	public abstract GrammaticalPerson getPerson();
	
	/**
	 * Gets the tense of the verb.
	 * @return The tense of the verb.
	 */
	public abstract VerbTense getTense();
	
	/**
	 * Gets the voice of the verb.
	 * @return The voice of the verb.
	 */
	public abstract VerbVoice getVoice();
}