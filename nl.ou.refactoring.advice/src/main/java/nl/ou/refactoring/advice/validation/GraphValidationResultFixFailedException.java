package nl.ou.refactoring.advice.validation;

/**
 * An exception that is thrown if an automated fix for a Refactoring Advice Graph (RAG) validation result fails.
 */
public abstract class GraphValidationResultFixFailedException extends RuntimeException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -5250849117745727875L;

	/**
	 * Initialises a new instance of {@link GraphValidationResultFixFailedException}.
	 */
	protected GraphValidationResultFixFailedException() {
		super();
	}
}