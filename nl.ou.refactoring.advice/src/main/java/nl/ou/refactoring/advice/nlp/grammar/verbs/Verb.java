package nl.ou.refactoring.advice.nlp.grammar.verbs;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.HasNumberAgreement;

/**
 * Represents a Verb in Natural Language grammar.
 */
public abstract class Verb
		implements HasNumberAgreement {
	private final long token;
	private final VerbTransitivity transitivity;
	private VerbAspect aspect;
	private VerbModality modality;
	private VerbTense tense;
	private VerbVoice voice;
	private GrammaticalNumber numberAgreement;
	
	/**
	 * Initialises a new instance of {@link Verb}.
	 * @param token The token that represents the verb.
	 * @param transitivity The transitivity of the verb.
	 */
	protected Verb(long token, VerbTransitivity transitivity) {
		this.token = token;
		this.transitivity = transitivity;
		this.aspect = VerbAspect.IMPERFECTIVE;
		this.modality = VerbModality.INDICATIVE;
		this.tense = VerbTense.PRESENT;
		this.voice = VerbVoice.ACTIVE;
		this.numberAgreement = GrammaticalNumber.SINGULAR;
	}
	
	/**
	 * Gets the token that represents the verb.
	 * @return The token that represents the verb.
	 */
	public final long getToken() {
		return this.token;
	}
	
	/**
	 * Gets the transitivity of the verb.
	 * @return The transitivity of the verb.
	 */
	public final VerbTransitivity getTransitivity() {
		return this.transitivity;
	}
	
	/**
	 * Gets the aspect of the verb.
	 * @return The aspect of the verb.
	 */
	public final VerbAspect getAspect() {
		return this.aspect;
	}
	
	/**
	 * Sets the aspect of the verb.
	 * @param aspect The aspect of the verb.
	 */
	public final void setAspect(VerbAspect aspect) {
		this.aspect = aspect;
	}
	
	/**
	 * Gets the modality of the verb.
	 * @return The modality of the verb.
	 */
	public final VerbModality getModality() {
		return this.modality;
	}
	
	/**
	 * Sets the modality of the verb.
	 * @param modality The modality of the verb.
	 */
	public final void setModality(VerbModality modality) {
		this.modality = modality;
	}
	
	/**
	 * Gets the tense of the verb.
	 * @return The tense of the verb.
	 */
	public final VerbTense getTense() {
		return this.tense;
	}
	
	/**
	 * Sets the tense of the verb.
	 * @param tense The tense of the verb.
	 */
	public final void setTense(VerbTense tense) {
		this.tense = tense;
	}
	
	/**
	 * Gets the voice of the verb.
	 * @return The voice of the verb.
	 */
	public final VerbVoice getVoice() {
		return this.voice;
	}
	
	/**
	 * Sets the voice of the verb.
	 * @param voice The voice of the verb.
	 */
	public final void setVoice(VerbVoice voice) {
		this.voice = voice;
	}
	
	@Override
	public final GrammaticalNumber getNumberAgreement() {
		return this.numberAgreement;
	}
	
	@Override
	public final void setNumberAgreement(GrammaticalNumber number) {
		this.numberAgreement = number;
	}
}