package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

class GraphWorkflowExplorerGetMicrostepsTestArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(getRenameField(), getRenameMethod());
	}

	private static Arguments getRenameField() {
		final var graph = new Graph("Rename Field");
		final var startNode = graph.start();
		final var addField = new GraphNodeMicrostepAddField(graph);
		final var removeField = new GraphNodeMicrostepRemoveField(graph);
		startNode.initiates(addField);
		addField.precedes(removeField);
		removeField.finalises();
		return Arguments.of(graph, Set.of(addField, removeField));
	}

	private static Arguments getRenameMethod() {
		final var graph = new Graph("Rename Method");
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		startNode.initiates(addMethod);
		addMethod.precedes(removeMethod);
		return Arguments.of(graph, Set.of(addMethod, removeMethod));
	}
}
