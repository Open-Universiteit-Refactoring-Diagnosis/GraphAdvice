package nl.ou.refactoring.advice;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
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

public class GraphTests {
	@Test
	@DisplayName("Should construct a graph")
	public void constructorTest() {
		Graph graph = new Graph();
		Assertions.assertNotNull(graph);
	}
	
	@Test
	@DisplayName("Shoud retrieve all nodes")
	public void getNodesHappyTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod addMethod = new GraphNodeMicrostepAddMethod(graph);
		
		// Act
		Set<GraphNode> nodes = graph.getNodes();
		
		// Assert
		Assertions.assertNotEquals(0, nodes.size());
	}
	
	@Test
	@DisplayName("Should retrieve nodes that are assignable to a particular node type")
	public void getNodesOfTypeHappyTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod addMethod = new GraphNodeMicrostepAddMethod(graph);
		GraphNodeMicrostepRemoveMethod removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		GraphNodeRemedyChooseDifferentName chooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		
		// Act
		Set<GraphNodeMicrostep> nodes = graph.getNodes(GraphNodeMicrostep.class);
		
		// Assert
		Assertions.assertEquals(2, nodes.size());
	}
	
	@Test
	@DisplayName("Should correctly determine whether the graph contains a particular node")
	public void containsNodeHappyTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod addMethod = new GraphNodeMicrostepAddMethod(graph);
		GraphNodeMicrostepRemoveMethod removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		GraphNodeRemedyChooseDifferentName chooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		
		// Act
		boolean containsNodeRemoveMethod = graph.containsNode(removeMethod);
		
		// Assert
		Assertions.assertTrue(containsNodeRemoveMethod);
	}
	
	@Test
	@DisplayName("Should add a new edge that is not already present in the graph")
	public void addEdgeNewTest() {
		// Arrange
		Graph graph = new Graph();
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
		GraphNodeMicrostepAddMethod createMethod = new GraphNodeMicrostepAddMethod(graph);
		
		// Act
		GraphEdge[] edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.getOrAddEdge(
				start,
				createMethod,
				(source, destination) -> new GraphEdgeInitiates(source, destination),
				GraphEdgeInitiates.class);
		GraphEdge[] edgesAfter = graph.getEdges().toArray(new GraphEdge[0]);
		
		// Assert
		Assertions.assertFalse(Arrays.equals(edgesBefore, edgesAfter));
		Assertions.assertEquals(0, edgesBefore.length);
		Assertions.assertEquals(1, edgesAfter.length);
		Assertions.assertTrue(GraphEdgeInitiates.class.isInstance(edgesAfter[0]));
		GraphEdgeInitiates initiates = (GraphEdgeInitiates)edgesAfter[0];
		Assertions.assertTrue(initiates.getSourceNode().equals(start));
		Assertions.assertTrue(initiates.getDestinationNode().equals(createMethod));
	}
	
	@Test
	@DisplayName("Should not add the same edge a second time")
	public void addEdgeExistingTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod createMethod = new GraphNodeMicrostepAddMethod(graph);
		GraphNodeMicrostepRemoveMethod removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		
		// Act
		graph.getOrAddEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		GraphEdge[] edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.getOrAddEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		GraphEdge[] edgesAfter = graph.getEdges().toArray(new GraphEdge[0]);
		
		// Assert
		Assertions.assertArrayEquals(edgesBefore, edgesAfter);
	}
}
