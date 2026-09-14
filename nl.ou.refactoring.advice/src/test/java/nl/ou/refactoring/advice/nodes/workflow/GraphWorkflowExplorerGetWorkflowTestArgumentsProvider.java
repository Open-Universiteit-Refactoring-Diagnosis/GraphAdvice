package nl.ou.refactoring.advice.nodes.workflow;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

public final class GraphWorkflowExplorerGetWorkflowTestArgumentsProvider implements ArgumentsProvider {
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
		final var startInitiates = startNode.initiates(addField);
		final var addFieldPrecedes = addField.precedes(removeField);
		final var removeFieldPrecedes = removeField.finalises();
		final var endNode = (GraphNodeRefactoringEnd) removeFieldPrecedes.getDestinationNode();

		final var expected = new GraphPath(startNode);
		expected.append(startInitiates, addField);
		expected.append(addFieldPrecedes, removeField);
		expected.append(removeFieldPrecedes, endNode);

		return Arguments.of(graph, expected);
	}

	private static Arguments getRenameMethod() {
		final var graph = new Graph("Rename Method");
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var startInitiates = startNode.initiates(addMethod);
		final var addMethodPrecedes = addMethod.precedes(removeMethod);
		final var removeMethodPrecedes = removeMethod.finalises();
		final var endNode = (GraphNodeRefactoringEnd) removeMethodPrecedes.getDestinationNode();

		final var expected = new GraphPath(startNode);
		expected.append(startInitiates, addMethod);
		expected.append(addMethodPrecedes, removeMethod);
		expected.append(removeMethodPrecedes, endNode);

		return Arguments.of(graph, expected);
	}
}
