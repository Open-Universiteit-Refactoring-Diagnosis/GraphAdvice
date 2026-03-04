package nl.ou.refactoring.advice.io;

/**
 * An exception that is thrown if a Refactoring Advice Graph writer fails.
 */
public abstract class GraphWriterException extends RuntimeException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -1232357598968430255L;

	/**
	 * Initialises a new instance of {@link GraphWriterException}.
	 */
	protected GraphWriterException() {
		super();
	}
	
	/**
	 * Initialises a new instance of {@link GraphWriterException}.
	 * @param cause Another exception that may have caused this exception.
	 */
	protected GraphWriterException(Throwable cause) {
		super(cause);
	}
}