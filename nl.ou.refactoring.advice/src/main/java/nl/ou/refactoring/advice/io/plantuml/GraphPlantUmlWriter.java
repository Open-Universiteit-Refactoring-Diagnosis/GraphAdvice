package nl.ou.refactoring.advice.io.plantuml;

import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;

public abstract class GraphPlantUmlWriter extends GraphStringWriter {
	private static final String START_UML = "@startuml";
	private static final String END_UML = "@enduml";
	
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
	
	protected final void writeStartUml(String diagramName) {
		this.printLine(String.format("%s %s", START_UML, diagramName));
	}
	
	protected final void writeEndUml() {
		this.printLine(END_UML);
	}
}
