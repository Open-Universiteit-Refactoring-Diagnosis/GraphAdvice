package nl.ou.refactoring.advice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

public final class MultipleEdgesOfSameTypeExceptionTests {
	@Test
	@DisplayName("Should return localised message")
	public void getLocalizedMessageTest() {
		// Arrange
		final var graph = new Graph();
		final var doubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		final var method = new GraphNodeOperation(graph, "testMethod");
		final var affects = doubleDefinition.affects(method);
		final var exception = new MultipleEdgesOfSameTypeException(affects);
		
		// Act
		Locale.setDefault(Locale.of("nl", "NL"));
		final var exceptionMessageDutch = exception.getLocalizedMessage();
		Locale.setDefault(Locale.of("en", "GB"));
		final var exceptionMessageEnglish = exception.getLocalizedMessage();
		
		// Assert
		assertTrue(exceptionMessageDutch.startsWith("Meerdere Zijden van hetzelfde type `nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects`"));
		assertTrue(exceptionMessageEnglish.startsWith("Multiple Edges of the same type `nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects`"));
	}
}
