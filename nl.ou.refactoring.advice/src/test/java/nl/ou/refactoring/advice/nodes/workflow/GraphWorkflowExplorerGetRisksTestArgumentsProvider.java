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
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

class GraphWorkflowExplorerGetRisksTestArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		return Stream.of(getRenameField(), getRenameMethod());
	}

	private static Arguments getRenameField() {
		final var graph = new Graph("Rename Field");
		final var startNode = graph.start();
		final var addField = new GraphNodeMicrostepAddField(graph);
		final var doubleDefinitionRisk = new GraphNodeRiskDoubleDefinition(graph);
		final var removeField = new GraphNodeMicrostepRemoveField(graph);
		startNode.initiates(addField);
		addField.causes(doubleDefinitionRisk);
		addField.precedes(removeField);
		removeField.finalises();
		return Arguments.of(graph, Set.of(doubleDefinitionRisk));
	}

	private static Arguments getRenameMethod() {
		final var graph = new Graph("Rename Method");
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var doubleDefinitionRisk = new GraphNodeRiskDoubleDefinition(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinitionRisk = new GraphNodeRiskMissingDefinition(graph);
		startNode.initiates(addMethod);
		addMethod.causes(doubleDefinitionRisk);
		addMethod.precedes(removeMethod);
		removeMethod.causes(missingDefinitionRisk);
		return Arguments.of(graph, Set.of(doubleDefinitionRisk, missingDefinitionRisk));
	}
}