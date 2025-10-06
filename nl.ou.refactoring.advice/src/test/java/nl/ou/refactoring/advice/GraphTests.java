package nl.ou.refactoring.advice;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeInitiates;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;

public final class GraphTests {
	@Test
	@DisplayName("Should construct a graph")
	public void constructorTest() {
		final var graph = new Graph();
		Assertions.assertNotNull(graph);
	}
	
	@Test
	@DisplayName("Shoud retrieve all nodes")
	public void getNodesHappyTest() {
		// Arrange
		final var graph = new Graph();
		new GraphNodeMicrostepAddMethod(graph);
		
		// Act
		Set<GraphNode> nodes = graph.getNodes();
		
		// Assert
		assertNotEquals(0, nodes.size());
	}
	
	@Test
	@DisplayName("Should retrieve nodes that are assignable to a particular node type")
	public void getNodesOfTypeHappyTest() {
		// Arrange
		final var graph = new Graph();
		new GraphNodeMicrostepAddMethod(graph);
		new GraphNodeMicrostepRemoveMethod(graph);
		new GraphNodeRemedyChooseDifferentName(graph);
		
		// Act
		Set<GraphNodeMicrostep> nodes = graph.getNodes(GraphNodeMicrostep.class);
		
		// Assert
		assertEquals(2, nodes.size());
	}
	
	@Test
	@DisplayName("Should correctly determine whether the graph contains a particular node")
	public void containsNodeHappyTest() {
		// Arrange
		final var graph = new Graph();
		new GraphNodeMicrostepAddMethod(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		new GraphNodeRemedyChooseDifferentName(graph);
		
		// Act
		final var containsNodeRemoveMethod = graph.containsNode(removeMethod);
		
		// Assert
		assertTrue(containsNodeRemoveMethod);
	}
	
	@Test
	@DisplayName("Should add a new edge that is not already present in the graph")
	public void addEdgeNewTest() {
		// Arrange
		final var graph = new Graph();
		GraphNodeRefactoringStart start;
		try {
			start = new GraphNodeRefactoringStart(graph);
		} catch (ArgumentNullException e) {
			e.printStackTrace();
			Assertions.fail("This should not happen: graph should not be null, failed test.");
			return;
		} catch (RefactoringMayContainOnlyOneStartNodeException e) {
			e.printStackTrace();
			Assertions.fail("Graph already contains a start node.");
			return;
		}
		final var createMethod = new GraphNodeMicrostepAddMethod(graph);
		
		// Act
		final var edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.getOrAddEdge(
				start,
				createMethod,
				(source, destination) -> new GraphEdgeInitiates(source, destination),
				GraphEdgeInitiates.class);
		final var edgesAfter = graph.getEdges().toArray(new GraphEdge[0]);
		
		// Assert
		assertFalse(Arrays.equals(edgesBefore, edgesAfter));
		assertEquals(0, edgesBefore.length);
		assertEquals(1, edgesAfter.length);
		assertTrue(GraphEdgeInitiates.class.isInstance(edgesAfter[0]));
		final var initiates = (GraphEdgeInitiates)edgesAfter[0];
		assertTrue(initiates.getSourceNode().equals(start));
		assertTrue(initiates.getDestinationNode().equals(createMethod));
	}
	
	@Test
	@DisplayName("Should not add the same edge a second time")
	public void addEdgeExistingTest() {
		// Arrange
		final var graph = new Graph();
		final var createMethod = new GraphNodeMicrostepAddMethod(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		
		// Act
		graph.getOrAddEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		final var edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.getOrAddEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		final var edgesAfter = graph.getEdges().toArray(new GraphEdge[0]);
		
		// Assert
		assertArrayEquals(edgesBefore, edgesAfter);
	}
}
