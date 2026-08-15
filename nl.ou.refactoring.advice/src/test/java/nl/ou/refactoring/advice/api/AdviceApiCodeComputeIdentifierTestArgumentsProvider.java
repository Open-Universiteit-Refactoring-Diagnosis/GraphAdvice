package nl.ou.refactoring.advice.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

final class AdviceApiCodeComputeIdentifierTestArgumentsProvider implements ArgumentsProvider {
	private static Arguments getIdentifierDoesNotExistHappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var identifier = "testIdentifier";
			final var identifierNode = AdviceApi.Code.computeIdentifier(graph, identifier);
			assertNotNull(identifierNode);
			final var identifierNodeUUID = identifierNode.getId();
			final var identifierNodes =
					graph.getNodes(GraphNodeIdentifier.class)
							.stream()
							.filter(node -> node.getIdentifier().equals(identifier))
							.collect(Collectors.toUnmodifiableList());
			assertEquals(1, identifierNodes.size());
			assertEquals(identifierNodeUUID, ((GraphNodeIdentifier) identifierNodes.get(0)).getId());
		};

		return Arguments.of(testExecutor);
	}

	private static Arguments getIdentifierExistsHappy() {
		final Runnable testExecutor = () -> {
			final var graph = new Graph("Test Graph");
			final var identifier = "testIdentifier";
			final var identifierNode = GraphNodeIdentifier.computeIdentifier(graph, identifier);
			final var identifierNodeNext = AdviceApi.Code.computeIdentifier(graph, identifier);
			assertNotNull(identifierNodeNext);
			assertEquals(identifierNode.getId(), identifierNodeNext.getId());
			assertEquals(identifierNode.getIdentifier(), identifierNodeNext.getIdentifier());
			final var identifierNodes =
					graph.getNodes(GraphNodeIdentifier.class)
							.stream()
							.filter(node -> node.getIdentifier().equals(identifier))
							.collect(Collectors.toUnmodifiableList());
			assertEquals(1, identifierNodes.size());
		};

		return Arguments.of(testExecutor);
	}

	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(getIdentifierDoesNotExistHappy(), getIdentifierExistsHappy());
	}
}
