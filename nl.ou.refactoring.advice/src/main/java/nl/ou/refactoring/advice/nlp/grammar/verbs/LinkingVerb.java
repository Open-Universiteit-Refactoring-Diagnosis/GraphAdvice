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
 * Represents a linking verb such as "to be" or "to become".
 */
public final class LinkingVerb extends Verb {
	/**
	 * Initialises a new instance of {@link LinkingVerb}.
	 * @param token The token that represents a particular Linking Verb.
	 * @param phrase The {@link VerbPhrase} that contains the {@link LinkingVerb}.
	 */
	public LinkingVerb(long token, VerbPhrase phrase) {
		super(token, VerbTransitivity.COPULAR, phrase);
	}
	
	/**
	 * Constructs an Linking Verb from the specified token.
	 * @param token The token that should represent a Linking Verb.
	 * @param phrase The {@link VerbPhrase} that contains this {@link LinkingVerb}.
	 * @return The constructed Linking Verb.
	 * @throws ArgumentMembershipException Thrown if the token does not represent a Linking Verb.
	 * @throws ArgumentNullException Thrown if phrase is null.
	 */
	public static Optional<LinkingVerb> fromToken(long token, VerbPhrase phrase)
			throws
				ArgumentMembershipException,
				ArgumentNullException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Linking.all(), "Linking Verbs");
		ArgumentGuard.requireNotNull(phrase, "phrase");
		return Optional.of(new LinkingVerb(token, phrase));
	}

	@Override
	public VerbAspect getAspect() {
		return this.getPhrase().getConjugation().aspect();
	}

	@Override
	public VerbModality getModality() {
		return this.getPhrase().getConjugation().modality();
	}

	@Override
	public GrammaticalNumber getNumber() {
		return this.getPhrase().getConjugation().number();
	}
	
	@Override
	public GrammaticalRegister getRegister() {
		return this.getPhrase().getConjugation().register();
	}

	@Override
	public GrammaticalPerson getPerson() {
		return this.getPhrase().getConjugation().person();
	}

	@Override
	public VerbTense getTense() {
		return this.getPhrase().getConjugation().tense();
	}

	@Override
	public VerbVoice getVoice() {
		return this.getPhrase().getConjugation().voice();
	}
}