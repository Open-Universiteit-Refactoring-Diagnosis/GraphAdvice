package nl.ou.refactoring.advice.io.json;

import java.io.IOException;

import nl.ou.refactoring.advice.io.GraphReaderException;

/**
 * An exception that is thrown during the processing of Refactoring Advice Graphs in JSON format.
 */
public class GraphJsonReaderException extends GraphReaderException {
	/**
	 * A generated serial version Unique Identifier.
	 */
	private static final long serialVersionUID = -6571488018113693664L;

	/**
	 * Initialises a new instance of {@link GraphJsonReaderException}.
	 */
	protected GraphJsonReaderException() { }
	
	/**
	 * Initialises a new instance of {@link GraphJsonReaderException}.
	 * @param cause The cause of the exception.
	 */
	protected GraphJsonReaderException(IOException cause) {
		super(cause);
	}
}
