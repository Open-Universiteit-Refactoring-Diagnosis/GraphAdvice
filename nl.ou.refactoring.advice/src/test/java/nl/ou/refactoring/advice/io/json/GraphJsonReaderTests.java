package nl.ou.refactoring.advice.io.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

public final class GraphJsonReaderTests {

	@Test
	@DisplayName("Should read an empty graph from JSON")
	public void readEmptyGraphTest()
			throws GraphReaderException {
		// Arrange
		final var json = JsonSamplesLoader.loadJson("/writeEmptyGraphTest.json");
		final var stringReader = new StringReader(json);
		final var graphJsonReader = new GraphJsonReader(stringReader);
		
		// Act
		final var graph = graphJsonReader.read();
		
		// Assert
		assertEquals("Empty graph", graph.getRefactoringName());
		assertEquals(0, graph.getNodes().size());
	}
	
	@Test
	@DisplayName("Should read a sample graph from JSON")
	public void readSampleGraphTest()
			throws
				ArgumentNullException,
				GraphReaderException,
				RefactoringMayContainOnlyOneStartNodeException {
		// Arrange
		final var json = JsonSamplesLoader.loadJson("/writeSampleGraphTest.json");
		final var stringReader = new StringReader(json);
		final var graphJsonReader = new GraphJsonReader(stringReader);
		
		// Act
		final var graphActual = graphJsonReader.read();
		
		// Assert
		final var graphExpected = new Graph("Sample graph");
		final var start = graphExpected.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graphExpected);
		final var addExpression = new GraphNodeMicrostepAddExpression(graphExpected);
		start.initiates(addMethod);
		addMethod.precedes(addExpression);
		addExpression.finalises();
		
		assertEquals(graphExpected.getNodes().size(), graphActual.getNodes().size());
		assertEquals(graphExpected.getEdges().size(), graphActual.getEdges().size());
	}
}
