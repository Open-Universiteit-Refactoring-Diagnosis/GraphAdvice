package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentMembershipException;
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
	 */
	public LexicalVerb(long token, VerbTransitivity transitivity)
			throws ArgumentMembershipException {
		super(token, transitivity);
		ArgumentGuard.requireItemOf(token, "token", Tokens.Verbs.Lexical.all(), "Lexical Verbs");
	}
	
	/**
	 * Gets the {@link LexicalVerb} associated with the specified token.
	 * @param token The token that represents a {@link LexicalVerb}.
	 * @return The requested {@link LexicalVerb}, if registered wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public static Optional<LexicalVerb> fromToken(long token) {
		return Tokens.Verbs.Lexical.get(token);
	}
}