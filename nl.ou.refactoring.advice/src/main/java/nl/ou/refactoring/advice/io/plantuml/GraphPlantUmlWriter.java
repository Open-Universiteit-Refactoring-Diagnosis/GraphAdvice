package nl.ou.refactoring.advice.io.plantuml;

import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;

public abstract class GraphPlantUmlWriter extends GraphStringWriter {
	/**
	 * Initialises a new instance of {@link GraphPlantUmlWriter}.
	 * @param stringWriter Writes text output. Cannot be null.
	 * @exception ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphPlantUmlWriter(StringWriter stringWriter) {
		super(stringWriter);
	}

	@Override
	public abstract void write(Graph graph)
			throws ArgumentNullException, GraphPathSegmentInvalidException;
}
