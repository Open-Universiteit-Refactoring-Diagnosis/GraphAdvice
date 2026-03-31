package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * An Auxiliary Verb in a Natural Language grammar.
 */
public final class AuxiliaryVerb extends Verb {
	private final Verb headVerb;
	private VerbConjugationKey conjugation = VerbConjugationKey.DEFAULT;
	
	/**
	 * Initialises a new instance of {@link AuxiliaryVerb}.
	 * @param token The token that represents the auxiliary verb.
	 * @param headVerb The head {@link Verb} that is supplemented by this {@link AuxiliaryVerb}.
	 * @throws ArgumentMembershipException Thrown if the token is not associated with an Auxiliary Verb.
	 * @throws ArgumentNullException Thrown if headVerb is null or headVerb's phrase is null.
	 */
	public AuxiliaryVerb(long token, Verb headVerb)
			throws ArgumentMembershipException, ArgumentNullException {
		super(token, VerbTransitivity.INTRANSITIVE, headVerb == null ? null : headVerb.getPhrase());
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Auxiliary.all(), "Auxiliary Verbs");
		ArgumentGuard.requireNotNull(headVerb, "headVerb");
		this.headVerb = headVerb;
		this.headVerb.setAuxiliaryVerb(this);
	}
	
	/**
	 * Constructs an Auxiliary Verb from the specified token.
	 * @param token The token that should represent an Auxiliary Verb.
	 * @param headVerb The head {@link Verb} that is supplemented by this {@link AuxiliaryVerb}.
	 * @return If found by token, the constructed Auxiliary Verb wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @throws ArgumentMembershipException Thrown if the token does not represent an Auxiliary Verb.
	 * @throws ArgumentNullException Thrown if headVerb is null.
	 */
	public static Optional<AuxiliaryVerb> setByToken(long token, Verb headVerb)
			throws ArgumentMembershipException, ArgumentNullException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Auxiliary.all(), "Auxiliary Verbs");
		ArgumentGuard.requireNotNull(headVerb, "headVerb");
		final var auxiliaryVerb = new AuxiliaryVerb(token, headVerb);
		auxiliaryVerb.setConjugation(headVerb.getPhrase().getConjugation());
		return Optional.of(auxiliaryVerb);
	}
	
	/**
	 * Gets the proper conjugation of the {@link AuxiliaryVerb}.
	 * @return The proper conjugation of the {@link AuxiliaryVerb}.
	 */
	public VerbConjugationKey getConjugation() {
		return this.conjugation;
	}
	
	/**
	 * Sets the proper conjugation of the {@link AuxiliaryVerb}.
	 * @param conjugation The proper conjugation of the {@link AuxiliaryVerb}.
	 * @throws ArgumentNullException Thrown if conjugation is null.
	 */
	public void setConjugation(VerbConjugationKey conjugation)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(conjugation, "conjugation");
		this.conjugation = conjugation;
	}
	
	/**
	 * Gets the head {@link Verb} that this {@link AuxiliaryVerb} applies to.
	 * @return The head {@link Verb} that this {@link AuxiliaryVerb} applies to.
	 */
	public Verb getHeadVerb() {
		return this.headVerb;
	}

	@Override
	public VerbAspect getAspect() {
		return this.conjugation.aspect();
	}

	@Override
	public VerbModality getModality() {
		return this.conjugation.modality();
	}
	
	@Override
	public GrammaticalNumber getNumber() {
		return this.conjugation.number();
	}
	
	@Override
	public GrammaticalRegister getRegister() {
		return this.conjugation.register();
	}
	
	@Override
	public GrammaticalPerson getPerson() {
		return this.conjugation.person();
	}

	@Override
	public VerbTense getTense() {
		return this.conjugation.tense();
	}

	@Override
	public VerbVoice getVoice() {
		return this.conjugation.voice();
	}
}