package nl.ou.refactoring.advice.nodes.workflow.risks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;

public final class GraphNodeRiskTests {
	@Test
	@DisplayName("Should get microsteps that neutralise the risk")
	public void getNeutralisersTest() {
		// Arrange
		Graph graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		addMethod.obsolesces(missingDefinition);
		addMethod.precedes(updateReferences);
		updateReferences.obsolesces(missingDefinition);
		updateReferences.precedes(removeMethod);
		removeMethod.causes(missingDefinition);
		
		// Act
		final var neutralisers = missingDefinition.getNeutralisers();
		
		// Assert
		assertTrue(neutralisers.contains(addMethod));
		assertTrue(neutralisers.contains(updateReferences));
	}
	
	@Test
	@DisplayName("Should get microsteps that neutralise the risk, but omit microsteps that are not in a chain")
	public void getNeutralisersInvalidChainOfMicrostepsTest() {
		// Arrange
		Graph graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		addMethod.obsolesces(missingDefinition);
		updateReferences.obsolesces(missingDefinition);
		updateReferences.precedes(removeMethod);
		removeMethod.causes(missingDefinition);
		
		// Act
		final var neutralisers = missingDefinition.getNeutralisers();
		
		// Assert
		assertEquals(0, neutralisers.size());
	}
}
