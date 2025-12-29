package nl.ou.refactoring.advice.io.mermaid;

import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;
import nl.ou.refactoring.advice.io.GraphWriter;

/**
 * A base class for {@link GraphWriter} implementations that write <a href="https://www.mermaidchart.com/">Mermaid</a> charts.
 */
public abstract class GraphMermaidWriter extends GraphStringWriter {
	/**
	 * Initialises a new instance of {@link GraphMermaidWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphMermaidWriter(StringWriter stringWriter)
			throws ArgumentNullException {
		super(stringWriter);
	}

	@Override
	public abstract void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException;
}
