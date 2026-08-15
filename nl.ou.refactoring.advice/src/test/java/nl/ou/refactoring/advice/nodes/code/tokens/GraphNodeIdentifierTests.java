package nl.ou.refactoring.advice.nodes.code.tokens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import nl.ou.refactoring.advice.Graph;

public final class GraphNodeIdentifierTests {
	@DisplayName("Should properly compute an identifier node")
	@ParameterizedTest
	@ValueSource(strings = { "abc", "abc123", "_abc123" })
	public void computeIdentifierTests(String input) {
		final var graph = new Graph("Identifier Test Graph");
		final var identifierNode = GraphNodeIdentifier.computeIdentifier(graph, input);
		assertNotNull(identifierNode);
	}

	@DisplayName("Should throw exception if identifier is invalid")
	@ParameterizedTest
	@ArgumentsSource(GraphNodeIdentifierComputeIdentifierInvalidTestsProvider.class)
	public void computeIdentifierInvalidTests(String input, Class<? extends Throwable> exceptionClassType) {
		final var graph = new Graph("Identifier Test Graph");
		assertThrows(exceptionClassType, () -> {
			GraphNodeIdentifier.computeIdentifier(graph, input);
		});
	}

	@DisplayName("Should compare two identifier nodes and indicate whether they are equal")
	@ParameterizedTest
	@ArgumentsSource(GraphNodeIdentifierTestsEqualsArgumentsProvider.class)
	public void equalsTests(GraphNodeIdentifier left, GraphNodeIdentifier right, boolean expected) {
		assertEquals(expected, left.equals(right));
	}
}