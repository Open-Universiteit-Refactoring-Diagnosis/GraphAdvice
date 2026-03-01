package nl.ou.refactoring.advice.io.javaParser;

import nl.ou.refactoring.advice.io.json.GraphJsonReaderException;

/**
 * An exception that is thrown if the {@link GraphJavaReader} fails to read a graph from Java code.s
 */
public abstract class GraphJavaReaderException extends GraphJsonReaderException {
	/**
	 * A generated serialised version unique identifier.
	 */
	private static final long serialVersionUID = 6915930818554349203L;

	/**
	 * Initialises a new instance of {@link GraphJavaReaderException}.
	 */
	protected GraphJavaReaderException() {
	}
	
	/**
	 * Initialises a new instance of {@link GraphJavaReaderException}.
	 * @param cause The cause of the exception.
	 */
	protected GraphJavaReaderException(Throwable cause) {
		super(cause);
	}
}