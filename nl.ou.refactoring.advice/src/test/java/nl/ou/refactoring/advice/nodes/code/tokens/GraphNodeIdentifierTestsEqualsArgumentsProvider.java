package nl.ou.refactoring.advice.nodes.code.tokens;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;

public final class GraphNodeIdentifierTestsEqualsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
		return Stream.of(
				Arguments.of(
						GraphNodeIdentifier.computeIdentifier(graph(), "Test"),
						GraphNodeIdentifier.computeIdentifier(graph(), "Test"),
						true),
				Arguments.of(
						GraphNodeIdentifier.computeIdentifier(graph(), "Test"),
						GraphNodeIdentifier.computeIdentifier(graph(), "Test2"),
						false));
	}

	private static Graph graph() {
		return new Graph("Identifier Equals Tests");
	}
}