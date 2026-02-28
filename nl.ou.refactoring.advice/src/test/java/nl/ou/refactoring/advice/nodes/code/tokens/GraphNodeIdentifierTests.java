package nl.ou.refactoring.advice.nodes.code.tokens;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;

public final class GraphNodeIdentifierTests {
	@DisplayName("Should properly construct an identifier node")
	@ParameterizedTest
	@ValueSource(strings = {"abc","abc123","_abc123"})
	public void constructorTests(String input) {
		final var graph = new Graph("Identifier Test Graph");
		final var identifierNode = new GraphNodeIdentifier(graph, input);
		assertNotNull(identifierNode);
	}
	
	@DisplayName("Should properly construct an identifier node")
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"1","@","abc def"})
	public void constructorInvalidTests(String input) {
		final var graph = new Graph("Identifier Test Graph");
		assertThrows(ArgumentPatternException.class, () -> {
			new GraphNodeIdentifier(graph, input);
		});
	}
}