package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * Represents a Lexical Verb that carries the primary meaning in a {@link Sentence}.
 */
public final class LexicalVerb extends Verb {
	/**
	 * Initialises a new instance of {@link LexicalVerb}.
	 * @param token The token that represents a particular Lexical Verb.
	 * @param transitivity The transitivity of the Lexical Verb.
	 * @throws ArgumentMembershipException Thrown if the token is not associated with a Lexical Verb.
	 * @throws ArgumentNullException Thrown if phrase is null.
	 */
	public LexicalVerb(long token, VerbTransitivity transitivity, VerbPhrase phrase)
			throws
				ArgumentMembershipException,
				ArgumentNullException {
		super(token, transitivity, phrase);
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Lexical.all(), "Lexical Verbs");
	}
	
	/**
	 * Gets the {@link LexicalVerb} associated with the specified token.
	 * @param token The token that represents a {@link LexicalVerb}.
	 * @return The requested {@link LexicalVerb}, if registered wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public static Optional<Function<VerbPhrase, Verb>> fromToken(long token)
			throws ArgumentNullException {
		return
			Tokens
				.Verbs
				.Lexical
				.get(token);
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