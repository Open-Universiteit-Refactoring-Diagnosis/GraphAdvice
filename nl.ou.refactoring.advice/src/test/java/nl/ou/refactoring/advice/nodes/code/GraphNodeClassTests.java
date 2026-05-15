package nl.ou.refactoring.advice.nodes.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameterSignature;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

public final class GraphNodeClassTests {

	@ParameterizedTest
	@MethodSource("getOperationNodeTestCases")
	@DisplayName("Should get the requested operation node if present, otherwise return empty")
	public void getOperationNodeTest(
		GraphNodeClass classNode,
		GraphNodeIdentifier operationName,
		List<GraphNodeOperationParameterSignature> operationParameterSignatures,
		Optional<GraphNodeOperation> expectedOperationNode
	) {
		final var actualOperationNode = classNode.getOperationNode(operationName.getIdentifier(), operationParameterSignatures);
		assertEquals(expectedOperationNode, actualOperationNode);
	}
	
	private static Stream<Arguments> getOperationNodeTestCases() {
		final var argumentsList = new ArrayList<Arguments>();
		
		final var graph1 = new Graph("Graph test simple operation");
		final var graph1IdentifierNode = new GraphNodeIdentifier(graph1, "Class1");
		final var graph1ClassNode = new GraphNodeClass(graph1, graph1IdentifierNode);
		final var graph1OperationNodeName = new GraphNodeIdentifier(graph1, "operation1");
		final var graph1OperationNodeParameterSignatures = new ArrayList<GraphNodeOperationParameterSignature>();
		final var graph1OperationNode = new GraphNodeOperation(graph1, graph1OperationNodeName);
		graph1ClassNode.has(graph1OperationNode);
		argumentsList.add(
			Arguments.of(
				graph1ClassNode,
				graph1OperationNodeName,
				graph1OperationNodeParameterSignatures,
				Optional.ofNullable(graph1OperationNode)
			)
		);
		
		final var graph2 = new Graph("Graph test operation with parameters");
		final var graph2IdentifierNode = new GraphNodeIdentifier(graph2, "Class2");
		final var graph2ClassNode = new GraphNodeClass(graph2, graph2IdentifierNode);
		final var graph2OperationNodeName = new GraphNodeIdentifier(graph2, "operation2");
		final var graph2OperationNodeParametersInput = new ArrayList<GraphNodeOperationParameterSignature>();
		graph2OperationNodeParametersInput.add(new GraphNodeOperationParameterSignature("test", null));
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
				Optional.ofNullable(graph2OperationNode)
			)
		);
		
		final var graph3 = new Graph("Graph test operation not found");
		final var graph3IdentifierNode = new GraphNodeIdentifier(graph3, "Class3");
		final var graph3ClassNode = new GraphNodeClass(graph3, graph3IdentifierNode);
		final var graph3OperationNodeName = new GraphNodeIdentifier(graph3, "operation3");
		final var graph3OperationNodeParameters = new ArrayList<GraphNodeOperationParameterSignature>();
		final GraphNodeOperation graph3OperationNode = null;
		argumentsList.add(
			Arguments.of(
				graph3ClassNode,
				graph3OperationNodeName,
				graph3OperationNodeParameters,
				Optional.ofNullable(graph3OperationNode)
			)
		);
		
		final var graph4 = new Graph("Graph test operation parameters mismatch");
		final var graph4IdentifierNode = new GraphNodeIdentifier(graph4, "Class4");
		final var graph4ClassNode = new GraphNodeClass(graph4, graph4IdentifierNode);
		final var graph4OperationNodeName = new GraphNodeIdentifier(graph4, "operation4");
		final var graph4OperationNodeParametersInput = new ArrayList<GraphNodeOperationParameterSignature>();
		graph4OperationNodeParametersInput.add(new GraphNodeOperationParameterSignature("test", ""));
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
				Optional.ofNullable(graph4OperationNodeExpected)
			)
		);
		
		return argumentsList.stream();
	}

}
