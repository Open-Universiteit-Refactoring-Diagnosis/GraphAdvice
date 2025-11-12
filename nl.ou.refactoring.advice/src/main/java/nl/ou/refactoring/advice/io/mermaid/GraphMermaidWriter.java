package nl.ou.refactoring.advice.io.mermaid;

import java.io.PrintWriter;
import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriter;

/**
 * A base class for {@link GraphWriter} implementations that write <a href="https://www.mermaidchart.com/">Mermaid</a> charts.
 */
public abstract class GraphMermaidWriter implements GraphWriter {
	private final String indent = "  ";
	private final PrintWriter printWriter;
	protected int indentIndex = 0;
	
	/**
	 * Initialises a new instance of {@link GraphMermaidWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphMermaidWriter(StringWriter stringWriter)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(stringWriter, "stringWriter");
		this.printWriter = new PrintWriter(stringWriter);
	}

	@Override
	public abstract void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException;
	
	/**
	 * Prints a line to the output {@link StringWriter}.
	 * @param text
	 */
	protected final void printLine(String text) {
		printWriter.println(indent.repeat(indentIndex) + text);
	}
}
