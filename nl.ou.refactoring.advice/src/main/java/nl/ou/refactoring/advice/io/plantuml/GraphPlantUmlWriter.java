package nl.ou.refactoring.advice.io.plantuml;

import java.awt.Color;
import java.io.StringWriter;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphStringWriter;

import static nl.ou.refactoring.advice.io.ColorExtensions.toHexadecimal;

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
	 * Writes a "skin parameter" directive for a PlantUML diagram.
	 * @param name The name of the skin.
	 * @param colour The colour of the skin.
	 */
	protected final void writeSkinParam(String name, Color colour) {
		this.printLine(String.format("skinparam %s %s", name, toHexadecimal(colour)));
	}
	
	/**
	 * Writes a "sprite" directive for a PlantUML diagram, that draws a bug icon.
	 * @param name The name of the sprite.
	 * @see Source: <a href="https://plantuml.com/sprite">PlantUML Sprites</a>
	 */
	protected final void writeSpriteBug(String name) {
		this.printLine(
				String.format(
						"sprite %s [15x15/16z] PKzR2i0m2BFMi15p__FEjQEqB1z27aeqCqixa8S4OT7C53cKpsHpaYPDJY_12MHM-BLRyywPhrrlw3qumqNThmXgd1TOterAZmOW8sgiJafogofWRwtV3nCF",
						name
				)
		);
	}
	
	/**
	 * Writes a "sprite" directive for a PlantUML diagram, that draws a disk icon.
	 * @param name The name of the sprite.
	 * @see Source: <a href="https://plantuml.com/sprite">PlantUML Sprites</a>
	 */
	protected final void writeSpriteDisk(String name) {
		final var separator = System.lineSeparator();
		this.printLine(
				String.format(
						"sprite %s {" + separator
						+ "   444445566677881" + separator
						+ "   436000000009991" + separator
						+ "   43600000000ACA1" + separator
						+ "   53700000001A7A1" + separator
						+ "   53700000012B8A1" + separator
						+ "   53800000123B8A1" + separator
						+ "   63800001233C9A1" + separator
						+ "   634999AABBC99B1" + separator
						+ "   744566778899AB1" + separator
						+ "   7456AAAAA99AAB1" + separator
						+ "   8566AFC228AABB1" + separator
						+ "   8567AC8118BBBB1" + separator
						+ "   867BD4433BBBBB1" + separator
						+ "   39AAAAABBBBBBC1" + separator
						+ "}",
						name
				)
		);
	}
	
	/**
	 * Writes the "enduml" directive of a PlantUML diagram.
	 */
	protected final void writeEndUml() {
		this.printLine(END_UML);
	}
}
