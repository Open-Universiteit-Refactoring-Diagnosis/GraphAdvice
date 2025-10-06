package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Set;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class GraphNodeRiskTests {
	@Test
	@DisplayName("Should get microsteps that neutralise the risk")
	public void getNeutralisersTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod addMethod = new GraphNodeMicrostepAddMethod(graph);
		GraphNodeMicrostepUpdateReferences updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		GraphNodeMicrostepRemoveMethod removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		GraphNodeRiskMissingDefinition missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		removeMethod.causes(missingDefinition);
		addMethod.obsolesces(missingDefinition);
		updateReferences.obsolesces(missingDefinition);
		
		// Act
		Set<GraphNode> neutralisers = missingDefinition.getNeutralisers();
		
		// Assert
		Assertions.assertTrue(neutralisers.contains(addMethod));
		Assertions.assertTrue(neutralisers.contains(updateReferences));
	}
}
