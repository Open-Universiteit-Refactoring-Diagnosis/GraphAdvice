package nl.ou.refactoring.advice.nodes.code.operations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;

public final class GraphNodeOperationParameterTests {
	@DisplayName("Should get the type of an operation parameter")
	@ParameterizedTest
	@MethodSource("getParameterTypeTestParameters")
	public void getParameterTypeTest(GraphNodeOperationParameter parameterNode, GraphNodeType parameterTypeNodeExpected) {
		final var parameterTypeNodeActual = parameterNode.getParameterType();
		assertEquals(parameterTypeNodeExpected, parameterTypeNodeActual);
	}
	
	public static Stream<Arguments> getParameterTypeTestParameters() {
		final var graph = new Graph("Operation Parameters graph");
		
		final var operationParameterWithoutType = new GraphNodeOperationParameter(graph, "parameterWithoutType");
		final var operationParameterWithString = new GraphNodeOperationParameter(graph, "parameterWithString");
		final var stringType = GraphNodeType.computeType(graph, "String");
		operationParameterWithString.is(stringType);
		
		return
			Stream.of(
				Arguments.of(operationParameterWithoutType, null),
				Arguments.of(operationParameterWithString, stringType)
			);
	}
}