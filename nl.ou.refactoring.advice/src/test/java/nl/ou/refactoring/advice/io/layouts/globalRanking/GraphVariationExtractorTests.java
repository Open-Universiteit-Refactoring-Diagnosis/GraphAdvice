package nl.ou.refactoring.advice.io.layouts.globalRanking;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;

public final class GraphVariationExtractorTests {
	@Test
	@DisplayName("Should correctly extract graph variations")
	public void extractTest()
			throws
				ArgumentNullException,
				ArgumentEmptyException,
				GraphPathSegmentInvalidException,
				RefactoringMayContainOnlyOneStartNodeException {
		// Arrange
		final String graphName = "Variations graph";
		final var graph = new Graph(graphName);
		final var start = new GraphNodeRefactoringStart(graph, graphName);
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		start.initiates(addMethod);
		addMethod.precedes(updateReferences);
		updateReferences.precedes(removeMethod);
		removeMethod.finalises();
		final int MAXIMUM_DEPTH = 3;
		
		// Act
		final var variations = GraphVariationExtractor.extract(graph, MAXIMUM_DEPTH);
		
		// Assert
		assertNotNull(variations);
	}
}
