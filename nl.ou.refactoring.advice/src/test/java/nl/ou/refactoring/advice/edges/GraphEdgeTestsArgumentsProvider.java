package nl.ou.refactoring.advice.edges;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;

public final class GraphEdgeTestsArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments
	(
		ParameterDeclarations parameters,
		ExtensionContext context
	) {
		final var argumentsList = new ArrayList<Arguments>();
		
		final var graphOriginal = new Graph("Graph Original");
		final var graphCloned = new Graph("Graph Cloned");
		
		final var graphNodePackageOriginal = new GraphNodePackage(graphOriginal, "nl.ou.refactoring");
		final var graphNodeClassOriginal = new GraphNodeClass(graphOriginal, "Alpha");
		final var graphEdgePackageHasClass = new GraphEdgeHas(graphNodePackageOriginal, graphNodeClassOriginal);
		argumentsList.add(
			Arguments.of(
				graphEdgePackageHasClass,
				new GraphNodePackage(graphCloned, "nl.ou.refactoring"),
				new GraphNodeClass(graphCloned, "Alpha")
			)
		);
		
		final var graphNodeAttributeOriginal = new GraphNodeAttribute(graphOriginal, "foo");
		final var graphEdgeClassHasAttribute = new GraphEdgeHas(graphNodeClassOriginal, graphNodeAttributeOriginal);
		argumentsList.add(
			Arguments.of(
				graphEdgeClassHasAttribute,
				new GraphNodeClass(graphCloned, "Alpha"),
				new GraphNodeAttribute(graphCloned, "foo")
			)
		);
		
		return argumentsList.stream();
	}
}
