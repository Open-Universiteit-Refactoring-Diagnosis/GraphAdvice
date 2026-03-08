package nl.ou.refactoring.advice.nlp;

/**
 * An exception that is thrown if a Natural Language Processing request fails.
 */
public abstract class NLPException extends RuntimeException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -8127676588958685124L;

	/**
	 * Initialises a new instance of {@link NLPException}.
	 */
	protected NLPException() { }
}