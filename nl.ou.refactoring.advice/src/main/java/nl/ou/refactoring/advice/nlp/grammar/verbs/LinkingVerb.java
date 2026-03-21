package nl.ou.refactoring.advice.nlp.grammar.verbs;

public final class LinkingVerb extends Verb {
	/**
	 * Initialises a new instance of {@link LinkingVerb}.
	 * @param token The token that represents a particular Linking Verb.
	 */
	public LinkingVerb(long token) {
		super(token, VerbTransitivity.COPULAR);
	}
}
