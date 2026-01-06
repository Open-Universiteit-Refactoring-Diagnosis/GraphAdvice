package nl.ou.refactoring.advice;

public abstract class GraphValidationException extends RuntimeException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = 5138700203295732056L;

	/**
	 * Initialises a new instance of {@link GraphValidationException}.
	 */
	protected GraphValidationException() {
		super();
	}
	
	/**
	 * Gets a localised exception message that contains relevant details.
	 * @return A localised exception message that contains relevant details.
	 */
	@Override
	public abstract String getLocalizedMessage();
}
