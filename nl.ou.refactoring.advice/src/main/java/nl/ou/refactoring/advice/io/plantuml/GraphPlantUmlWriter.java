package nl.ou.refactoring.advice.io.plantuml;

import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;

/**
 * An abstract implementation of a writer that writes PlantUML specifications.
 */
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
			throws
				ArgumentNullException,
				GraphPathSegmentInvalidException;
	
	/**
	 * Writes the "startuml" directive of a PlantUML diagram.
	 * @param diagramName The name of the diagram.
	 */
	protected final void writeStartUml(String diagramName) {
		this.printLine(String.format("%s %s", START_UML, diagramName));
	}
	
	/**
	 * Writes a "set separator" directive for a PlantUML diagram.
	 * @param separator The separator in the diagram.
	 */
	protected final void writeSetSeparator(String separator) {
		this.printLine(String.format("set separator %s", separator));
	}
	
	/**
	 * Writes the "enduml" directive of a PlantUML diagram.
	 */
	protected final void writeEndUml() {
		this.printLine(END_UML);
	}
}
