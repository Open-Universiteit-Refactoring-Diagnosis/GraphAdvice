package nl.ou.refactoring.advice.io.svg;

import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;
import nl.ou.refactoring.advice.io.GraphWriterException;

/**
 * An abstract implementation of a writer that writes Scalable Vector Graphics (SVG) specifications.
 */
public abstract class GraphSvgWriter extends GraphStringWriter {
	/**
	 * Initialises a new instance of {@link GraphSvgWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphSvgWriter(StringWriter stringWriter) {
		super(stringWriter);
	}

	@Override
	public abstract void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException;
}