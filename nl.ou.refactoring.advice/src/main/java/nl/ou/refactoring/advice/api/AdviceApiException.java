package nl.ou.refactoring.advice.api;

/**
 * An exception that is thrown by the Application Programming Interface (API) of Graph Advice.
 */
public abstract class AdviceApiException extends RuntimeException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 7596959305711958737L;

	/**
	 * Initialises a new instance of {@link AdviceApiException}.
	 */
	protected AdviceApiException() {
		super();
	}

	/**
	 * Initialises a new instance of {@link AdviceApiException}.
	 * @param cause The cause of the exception.
	 */
	protected AdviceApiException(Throwable cause) {
		super(cause);
	}

	@Override
	public abstract String getLocalizedMessage();
}