package nl.ou.refactoring.advice;

import java.io.StringReader;
import java.util.Scanner;

import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.io.json.GraphJsonReader;

/**
 * Accesses JSON resource files that contain default Refactoring Advice Graphs.
 */
public final class GraphTemplates {

	private GraphTemplates() { }
	
	/**
	 * A "Move Method" Refactoring Advice Graph.
	 * @return The "Move Method" Refactoring Advice Graph as contained in the JSON file.
	 * @throws GraphReaderException Thrown if reading the JSON file failed.
	 */
	public static Graph moveMethod()
			throws GraphReaderException {
		return readGraph("/refactorings/MoveMethod.json");
	}
	
	/**
	 * A "Rename Field" Refactoring Advice Graph.
	 * @return The "Rename Field" Refactoring Advice Graph as contained in the JSON file.
	 * @throws GraphReaderException Thrown if reading the JSON file failed.
	 */
	public static Graph renameField()
			throws GraphReaderException {
		return readGraph("/refactorings/RenameField.json");
	}
	
	/**
	 * A "Rename Method" Refactoring Advice Graph.
	 * @return The "Rename Method" Refactoring Advice Graph as contained in the JSON file.
	 * @throws GraphReaderException Thrown if reading the JSON file failed.
	 */
	public static Graph renameMethod()
			throws GraphReaderException {
		return readGraph("/refactorings/RenameMethod.json");
	}

	private static String loadJson(String resourceName) {
		final var jsonResourceStream =
				GraphTemplates
					.class
					.getResourceAsStream(resourceName);
		final var jsonScanner =
				new Scanner(jsonResourceStream)
					.useDelimiter("\\A");
		final var json =
				jsonScanner.hasNext()
					? jsonScanner.next()
					: "";
		jsonScanner.close();
		return json;
	}
	
	private static Graph readGraph(String resourceName)
			throws GraphReaderException {
		final var renameMethodJson = loadJson(resourceName);
		final var stringReader = new StringReader(renameMethodJson);
		final var graphJsonReader = new GraphJsonReader(stringReader);
		return graphJsonReader.read();
	}
}
