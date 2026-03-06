package nl.ou.refactoring.advice.io.svg;

import javax.xml.parsers.ParserConfigurationException;

import nl.ou.refactoring.advice.io.GraphWriterException;

/**
 * An exception that is thrown if XML processing failed during the writing of SVG mark-up.
 */
public final class GraphSvgXmlException extends GraphWriterException {
	/**
	 * A serial version unique identifier.
	 */
	private static final long serialVersionUID = 4264770408937145722L;

	/**
	 * Initialises a new instance of {@link GraphSvgXmlException}.
	 * @param cause The cause of the exception.
	 */
	public GraphSvgXmlException(ParserConfigurationException cause) {
		super(cause);
	}
}