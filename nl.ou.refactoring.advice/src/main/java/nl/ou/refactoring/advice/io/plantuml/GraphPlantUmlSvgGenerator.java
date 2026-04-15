package nl.ou.refactoring.advice.io.plantuml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Generates Scalable Vector Graphics (SVG) from a PlantUML diagram specification.
 */
public class GraphPlantUmlSvgGenerator {
	private GraphPlantUmlSvgGenerator() { }
	
	/**
	 * Generates the raw SVG content from the specified PlantUML specification.
	 * @param puml The PlantUML specification.
	 * @return The raw SVG content that was generated from the specified PlantUML specification.
	 * @throws ArgumentNullException Thrown if puml is null.
	 * @throws ArgumentEmptyException Thrown if puml is an empty string or contains only white spaces.
	 * @throws IOException Thrown if there was a write error to the output stream that contains the raw SVG content.
	 */
	public static String generate(String puml)
			throws ArgumentNullException, ArgumentEmptyException, IOException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(puml, "puml");
		
		final var reader = new SourceStringReader(puml);
		final var outputStream = new ByteArrayOutputStream();
		reader.generateImage(outputStream, new FileFormatOption(FileFormat.SVG));
		outputStream.close();
		
		return outputStream.toString("UTF-8");
	}
}