package nl.ou.refactoring.advice.io.json;

import java.io.Reader;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReader;

/**
 * Reads Refactoring Advice Graphs from JSON.
 */
public class GraphJsonReader implements GraphReader {
	private final Reader reader;

	/**
	 * Initialises a new instance of {@link GraphJsonReader}.
	 * @param reader Reads JSON.
	 * @throws ArgumentNullException Thrown if reader is null.
	 */
	public GraphJsonReader(Reader reader)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(reader, "reader");
		this.reader = reader;
	}

	@Override
	public Graph read() {
		return null;
	}
}
