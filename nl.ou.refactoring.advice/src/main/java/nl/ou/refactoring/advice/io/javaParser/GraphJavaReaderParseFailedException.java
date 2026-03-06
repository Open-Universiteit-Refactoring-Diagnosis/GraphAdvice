package nl.ou.refactoring.advice.io.javaParser;

import com.github.javaparser.ParseProblemException;

/**
 * An exception that is thrown if the {@link GraphJavaReader} could not parse the Java code input.
 */
public final class GraphJavaReaderParseFailedException extends GraphJavaReaderException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 1020261243386837805L;

	/**
	 * Initialises a new instance of {@link GraphJavaReaderParseFailedException}.
	 * @param cause {@link ParseProblemException} The parser failure.
	 */
	public GraphJavaReaderParseFailedException(ParseProblemException cause) {
		super(cause);
	}
}