package nl.ou.refactoring.advice.nodes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

/**
 * Tests for deep cloning of Refactoring Advice Graph (RAGs).
 */
public final class GraphNodeDeepCloneTests {
	@DisplayName("Should deep clone a Refactoring Advice Graph (RAG)")
	@Test
	public void deepCloneTest() {
		final var graph = new Graph("Clone test");
		
		// AST
		GraphNodePackage.parse(graph, "nl.ou.refactoring.test");
		final var packageNodeLeaf = graph.getNode("nl.ou.refactoring.test", GraphNodePackage.class).get();
		final var classNodeLegacyEmployee = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "LegacyEmployee"));
		final var classNodeEmployee = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "Employee"));
		final var classNodeCompany = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "Company"));
		packageNodeLeaf.has(classNodeLegacyEmployee);
		packageNodeLeaf.has(classNodeEmployee);
		classNodeEmployee.is(classNodeLegacyEmployee);
		packageNodeLeaf.has(classNodeCompany);
		
		// Workflow
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethod);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(removeMethod);
		removeMethod.finalises();
		
		final var graphClone = (Graph)graph.clone(graph.getRefactoringName() + " (Cloned)");
		assertEquals("Clone test (Cloned)", graphClone.getRefactoringName());
		final var graphNodesCloned = graphClone.getNodes();
		assertEquals(graph.getNodes().size(), graphNodesCloned.size());
		final var graphEdgesCloned = graphClone.getEdges();
		assertEquals(graph.getEdges().size(), graphEdgesCloned.size());
	}
}
