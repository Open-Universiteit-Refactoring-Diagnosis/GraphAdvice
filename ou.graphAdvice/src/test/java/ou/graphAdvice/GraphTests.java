package ou.graphAdvice;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.edges.refactoring.GraphEdgeInitiates;
import ou.graphAdvice.edges.refactoring.GraphEdgePrecedes;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoringStart;
import ou.graphAdvice.nodes.refactoring.RefactoringMayContainOnlyOneStartNodeException;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostepAddMethod;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostepRemoveMethod;

public class GraphTests {
	@Test
	public void constructorTest() {
		Graph graph = new Graph();
		Assertions.assertNotNull(graph);
	}
	
	@Test
	public void addEdgeNewTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeRefactoringStart start;
		try {
			start = new GraphNodeRefactoringStart(graph);
		} catch (ArgumentNullException e) {
			e.printStackTrace();
			Assertions.fail("This should not happen.");
			return;
		} catch (RefactoringMayContainOnlyOneStartNodeException e) {
			e.printStackTrace();
			Assertions.fail("Graph already contains a start node.");
			return;
		}
		GraphNodeMicrostepAddMethod createMethod = new GraphNodeMicrostepAddMethod(graph);
		
		// Act
		GraphEdge[] edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.addEdge(
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
		Assertions.assertTrue(initiates.getSource().equals(start));
		Assertions.assertTrue(initiates.getDestination().equals(createMethod));
	}
	
	@Test
	public void addEdgeExistingTest() {
		// Arrange
		Graph graph = new Graph();
		GraphNodeMicrostepAddMethod createMethod = new GraphNodeMicrostepAddMethod(graph);
		GraphNodeMicrostepRemoveMethod removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		
		// Act
		graph.addEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		GraphEdge[] edgesBefore = graph.getEdges().toArray(new GraphEdge[0]);
		graph.addEdge(
				createMethod,
				removeMethod,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
		GraphEdge[] edgesAfter = graph.getEdges().toArray(new GraphEdge[0]);
		
		// Assert
		Assertions.assertArrayEquals(edgesBefore, edgesAfter);
	}
}
