package nl.ou.refactoring.advice.io.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

public final class GraphJsonWriterTests {
	@Test
	@DisplayName("Should write an empty graph to JSON")
	public void writeEmptyGraphTest() throws ArgumentNullException, GraphPathSegmentInvalidException {
		// Arrange
		final var graph = new Graph("Empty graph");
		final var stringWriter = new StringWriter();
		final var graphJsonWriter = new GraphJsonWriter(stringWriter);

		// Act
		graphJsonWriter.write(graph);
		final var jsonActual = stringWriter.toString().replace("\r\n", "\n");

		// Assert
		final var jsonExpected = JsonSamplesLoader.loadJson("/writeEmptyGraphTest.json").replace("\r\n", "\n");
		assertEquals(jsonExpected, jsonActual);
	}

	@Test
	@DisplayName("Should write a sample graph to JSON")
	public void writeSampleGraphTest() throws ArgumentNullException, GraphPathSegmentInvalidException,
			RefactoringMayContainOnlyOneStartNodeException {
		// Arrange
		final var graph = new Graph("Sample graph");
		final var start = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		start.initiates(addMethod);
		addMethod.precedes(addExpression);
		addExpression.finalises();

		final var stringWriter = new StringWriter();
		final var graphJsonWriter = new GraphJsonWriter(stringWriter);

		// Act
		graphJsonWriter.write(graph);
		final var jsonActual = stringWriter.toString().replace("\r\n", "\n");

		// Assert
		final var jsonExpected = JsonSamplesLoader.loadJson("/writeSampleGraphTest.json").replace("\r\n", "\n");
		assertEquals(jsonExpected, jsonActual);
	}
}
