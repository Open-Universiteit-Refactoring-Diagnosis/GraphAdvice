package nl.ou.refactoring.advice.io.text;

import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;
import nl.ou.refactoring.advice.io.GraphWriterException;

/**
 * Base class for generating text from Refactoring Advice Graphs.
 */
public abstract class GraphTextWriter extends GraphStringWriter {
	/**
	 * Initialises a new instance of {@link GraphTextWriter}.
	 * @param stringWriter The {@link StringWriter} responsible for text output.
	 */
	protected GraphTextWriter(StringWriter stringWriter) {
		super(stringWriter);
	}
	
	@Override
	public abstract void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException;
}