package nl.ou.refactoring.advice.edges;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.nodes.GraphNode;

public final class GraphEdgeTests {
	@DisplayName("Should properly clone a graph edge")
	@ParameterizedTest
	@ArgumentsSource(GraphEdgeTestsArgumentsProvider.class)
	public void cloneTests(GraphEdge original, GraphNode sourceCloned, GraphNode destinationCloned) {
		final var clone = original.clone(sourceCloned, destinationCloned);
		assertNotNull(clone);
	}
}
