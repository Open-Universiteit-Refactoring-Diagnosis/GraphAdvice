package nl.ou.refactoring.advice.io.json;

import java.io.StringReader;
import java.util.Scanner;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.io.GraphReaderException;

public final class GraphJsonDefaults {

	private GraphJsonDefaults() { }
	
	public static Graph renameField()
			throws GraphReaderException {
		return readGraph("/RenameField.json");
	}
	
	public static Graph renameMethod()
			throws GraphReaderException {
		return readGraph("/RenameMethod.json");
	}

	private static String loadJson(String resourceName) {
		final var jsonResourceStream =
				GraphJsonDefaults
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
