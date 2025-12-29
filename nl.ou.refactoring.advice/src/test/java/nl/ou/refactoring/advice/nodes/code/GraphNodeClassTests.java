package nl.ou.refactoring.advice.nodes.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;

public final class GraphNodeClassTests {

	@ParameterizedTest
	@MethodSource("getOperationNodeTestCases")
	@DisplayName("Should get the requested operation node if present, otherwise return null")
	public void getOperationNodeTest(
			GraphNodeClass classNode,
			String operationName,
			List<GraphNodeOperationParameter> operationParameters,
			GraphNodeOperation expectedOperationNode
	) {
		final var actualOperationNode = classNode.getOperationNode(operationName, operationParameters);
		assertEquals(expectedOperationNode, actualOperationNode);
	}
	
	private static Stream<Arguments> getOperationNodeTestCases() {
		final var argumentsList = new ArrayList<Arguments>();
		
		final var graph1 = new Graph("Graph test simple operation");
		final var graph1ClassNode = new GraphNodeClass(graph1, "Class 1");
		final var graph1OperationNodeName = "Operation 1";
		final var graph1OperationNodeParameters = new ArrayList<GraphNodeOperationParameter>();
		final var graph1OperationNode = new GraphNodeOperation(graph1, graph1OperationNodeName);
		graph1ClassNode.has(graph1OperationNode);
		argumentsList.add(
			Arguments.of(
				graph1ClassNode,
				graph1OperationNodeName,
				graph1OperationNodeParameters,
				graph1OperationNode
			)
		);
		
		final var graph2 = new Graph("Graph test operation with parameters");
		final var graph2ClassNode = new GraphNodeClass(graph2, "Class 2");
		final var graph2OperationNodeName = "Operation 2";
		final var graph2OperationNodeParametersInput = new ArrayList<GraphNodeOperationParameter>();
		graph2OperationNodeParametersInput.add(new GraphNodeOperationParameter(graph2, "test"));
		final var graph2OperationNodeParametersModel = new ArrayList<GraphNodeOperationParameter>();
		graph2OperationNodeParametersModel.add(new GraphNodeOperationParameter(graph2, "test"));
		final var graph2OperationNode =
			new GraphNodeOperation(
				graph2,
				graph2OperationNodeName,
				graph2OperationNodeParametersModel
			);
		graph2ClassNode.has(graph2OperationNode);
		argumentsList.add(
			Arguments.of(
				graph2ClassNode,
				graph2OperationNodeName,
				graph2OperationNodeParametersInput,
				graph2OperationNode
			)
		);
		
		final var graph3 = new Graph("Graph test operation not found");
		final var graph3ClassNode = new GraphNodeClass(graph3, "Class 3");
		final var graph3OperationNodeName = "Operation 3";
		final var graph3OperationNodeParameters = new ArrayList<GraphNodeOperationParameter>();
		final GraphNodeOperation graph3OperationNode = null;
		argumentsList.add(
			Arguments.of(
				graph3ClassNode,
				graph3OperationNodeName,
				graph3OperationNodeParameters,
				graph3OperationNode
			)
		);
		
		final var graph4 = new Graph("Graph test operation parameters mismatch");
		final var graph4ClassNode = new GraphNodeClass(graph4, "Class 4");
		final var graph4OperationNodeName = "Operation 4";
		final var graph4OperationNodeParametersInput = new ArrayList<GraphNodeOperationParameter>();
		graph4OperationNodeParametersInput.add(new GraphNodeOperationParameter(graph4, "test"));
		final var graph4OperationNodeParametersModel = new ArrayList<GraphNodeOperationParameter>();
		graph4OperationNodeParametersModel.add(new GraphNodeOperationParameter(graph4, "test2"));
		final var graph4OperationNodeModel =
			new GraphNodeOperation(
					graph4,
					graph4OperationNodeName,
					graph4OperationNodeParametersModel
			);
		graph4ClassNode.has(graph4OperationNodeModel);
		final GraphNodeOperation graph4OperationNodeExpected = null;
		argumentsList.add(
			Arguments.of(
				graph4ClassNode,
				graph4OperationNodeName,
				graph4OperationNodeParametersInput,
				graph4OperationNodeExpected
			)
		);
		
		return argumentsList.stream();
	}

}
