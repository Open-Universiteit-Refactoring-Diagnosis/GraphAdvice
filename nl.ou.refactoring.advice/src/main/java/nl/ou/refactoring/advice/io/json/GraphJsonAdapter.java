package nl.ou.refactoring.advice.io.json;

import java.io.Reader;
import java.io.Writer;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphAdapter;

/**
 * Reads and writes Refactoring Advice Graphs from and to JSON.
 */
public final class GraphJsonAdapter implements GraphAdapter {
	private final GraphJsonReader reader;
	private final GraphJsonWriter writer;
	
	/**
	 * Initialises a new instance of {@link GraphJsonAdapter}.
	 * @param reader Reads JSON.
	 * @param writer Writes JSON.
	 * @throws ArgumentNullException Thrown if reader or writer is null.
	 */
	public GraphJsonAdapter(Reader reader, Writer writer)
			throws ArgumentNullException {
		this.reader = new GraphJsonReader(reader);
		this.writer = new GraphJsonWriter(writer);
	}

	@Override
	public Graph read() {
		return this.reader.read();
	}

	@Override
	public void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		this.writer.write(graph);
	}
}
