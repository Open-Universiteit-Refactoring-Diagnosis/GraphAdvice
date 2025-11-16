package nl.ou.refactoring.advice.io;

/**
 * An exception that is thrown when a {@link GraphReader} encounters a fault.
 */
public abstract class GraphReaderException extends Exception {
	/**
	 * A generated serial version Unique Identifier.
	 */
	private static final long serialVersionUID = 9118184981154051620L;

	/**
	 * Initialises a new instance of {@link GraphReaderException}.
	 */
	protected GraphReaderException() { }
	
	/**
	 * Initialises a new instance of {@link GraphReaderException}.
	 * @param cause The cause of the exception.
	 */
	protected GraphReaderException(Throwable cause) {
		super(cause);
	}
}