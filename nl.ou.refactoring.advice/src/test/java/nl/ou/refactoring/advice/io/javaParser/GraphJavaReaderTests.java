package nl.ou.refactoring.advice.io.javaParser;

import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;

public final class GraphJavaReaderTests {
	@Test
	@DisplayName("Should read Java code to a graph")
	public void readTest() {
		final var graph = new Graph("Refactoring test");
		final var reader = new StringReader("package nl.ou.refactoring;\r\npublic class Test { }");
		final var javaReader = new GraphJavaReader(graph, reader);
		javaReader.read();
	}
}