package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * An auxiliary verb.
 */
public final class AuxiliaryVerb extends Verb {
	/**
	 * Initialises a new instance of {@link AuxiliaryVerb}.
	 * @param token The token that represents the auxiliary verb.
	 * @throws ArgumentMembershipException Thrown if the token is not associated with an Auxiliary Verb.
	 */
	public AuxiliaryVerb(long token)
			throws ArgumentMembershipException {
		super(token, VerbTransitivity.INTRANSITIVE);
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Auxiliary.all(), "Auxiliary Verbs");
	}
	
	/**
	 * Constructs an Auxiliary Verb from the specified token.
	 * @param token The token that should represent an Auxiliary Verb.
	 * @return The constructed Auxiliary Verb.
	 * @throws ArgumentMembershipException Thrown if the token does not represent an Auxiliary Verb.
	 */
	public static Optional<AuxiliaryVerb> fromToken(long token)
			throws ArgumentMembershipException {
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Auxiliary.all(), "Auxiliary Verbs");
		return Optional.of(new AuxiliaryVerb(token));
	}
}