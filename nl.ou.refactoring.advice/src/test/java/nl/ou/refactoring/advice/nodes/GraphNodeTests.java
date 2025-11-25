package nl.ou.refactoring.advice.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

public final class GraphNodeTests {
	@ParameterizedTest
	@MethodSource("findPathsToDestinationNodeTestProvider")
	@DisplayName("Should find paths to a destination node")
	public void findPathsToDestinationNodeTest(
			GraphNode sourceNode,
			GraphNode destinationNode,
			int maximumDepth,
			List<GraphPath> expected) {
		final var actual = sourceNode.findPaths(destinationNode, maximumDepth);
		Assertions.assertArrayEquals(expected.toArray(), actual.toArray());
	}
	
	private static Stream<Arguments> findPathsToDestinationNodeTestProvider()
			throws RefactoringMayContainOnlyOneStartNodeException, GraphPathSegmentInvalidException {
		final List<Arguments> testCases = new ArrayList<>();
		
		final var testCase1Graph = new Graph("Test Case single node");
		final var testCase1GraphStart = testCase1Graph.start();
		final var testCase1Expected = new ArrayList<GraphPath>();
		testCase1Expected.add(new GraphPath(testCase1GraphStart));
		testCases.add(Arguments.of(testCase1GraphStart, testCase1GraphStart, 5, testCase1Expected));
		
		final var testCase2Graph = new Graph("Test Case two nodes");
		final var testCase2GraphStart = testCase2Graph.start();
		final var testCase2GraphNodeMicrostep = new GraphNodeMicrostepAddMethod(testCase2Graph);
		final var testCase2GraphNodeEdge = testCase2GraphStart.initiates(testCase2GraphNodeMicrostep);
		final var testCase2Expected = new ArrayList<GraphPath>();
		final var testCase2ExpectedPath1 = new GraphPath(testCase2GraphStart);
		testCase2ExpectedPath1.append(testCase2GraphNodeEdge, testCase2GraphNodeMicrostep);
		testCase2Expected.add(testCase2ExpectedPath1);
		testCases.add(Arguments.of(testCase2GraphStart, testCase2GraphNodeMicrostep, 5, testCase2Expected));
		
		final var testCase3Graph = new Graph("Test Case more than two nodes");
		final var testCase3GraphStart = testCase3Graph.start();
		final var testCase3GraphNodeMicrostep1 = new GraphNodeMicrostepAddMethod(testCase3Graph);
		final var testCase3GraphNodeEdgeInitiates = testCase3GraphStart.initiates(testCase3GraphNodeMicrostep1);
		final var testCase3GraphNodeMicrostep2 = new GraphNodeMicrostepAddExpression(testCase3Graph);
		final var testCase3GraphNodeEdgePrecedes = testCase3GraphNodeMicrostep1.precedes(testCase3GraphNodeMicrostep2);
		// Path to third node
		final var testCase3Expected1 = new ArrayList<GraphPath>();
		final var testCase3Expected1Path1 = new GraphPath(testCase3GraphStart);
		testCase3Expected1Path1.append(testCase3GraphNodeEdgeInitiates, testCase3GraphNodeMicrostep1);
		testCase3Expected1Path1.append(testCase3GraphNodeEdgePrecedes, testCase3GraphNodeMicrostep2);
		testCase3Expected1.add(testCase3Expected1Path1);
		// Path to second node
		final var testCase3Expected2 = new ArrayList<GraphPath>();
		final var testCase3Expected2Path1 = new GraphPath(testCase3GraphStart);
		testCase3Expected2Path1.append(testCase3GraphNodeEdgeInitiates, testCase3GraphNodeMicrostep1);
		testCase3Expected2.add(testCase3Expected2Path1);
		// Path exceeds maximum length
		final var testCase3Expected3 = new ArrayList<GraphPath>();
		testCases.add(Arguments.of(testCase3GraphStart, testCase3GraphNodeMicrostep2, 5, testCase3Expected1));
		testCases.add(Arguments.of(testCase3GraphStart, testCase3GraphNodeMicrostep1, 5, testCase3Expected2));
		testCases.add(Arguments.of(testCase3GraphStart, testCase3GraphNodeMicrostep2, 1, testCase3Expected3));
		
		return testCases.stream();
	}
}