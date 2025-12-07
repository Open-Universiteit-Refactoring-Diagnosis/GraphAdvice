package nl.ou.refactoring.advice.io;

import java.io.PrintWriter;
import java.io.StringWriter;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A base class of a Refactoring Advice Graph (RAG) writer that writes to text.
 */
public abstract class GraphStringWriter implements GraphWriter {
	private static final String INDENT = "  ";
	private final PrintWriter printWriter;
	protected int indentIndex = 0;

	/**
	 * Initialises a new instance of {@link GraphStringWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphStringWriter(StringWriter stringWriter)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(stringWriter, "stringWriter");
		this.printWriter = new PrintWriter(stringWriter);
	}

	@Override
	public abstract void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException;

	/**
	 * Prints a line to the output {@link StringWriter}.
	 * @param text The text to write on the line.
	 */
	protected final void printLine(String text) {
		this.printWriter.println(INDENT.repeat(indentIndex) + text);
	}
}