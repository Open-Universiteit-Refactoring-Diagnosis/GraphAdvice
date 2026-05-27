package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates whether Missing Definition risk nodes are present where they are
 * expected.
 */
public final class GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator implements GraphValidator {
	/**
	 * A singleton instance of {@link GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator}.
	 */
	public static final GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator INSTANCE = new GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator();

	/**
	 * Initialises a new instance of
	 * {@link GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator}.
	 */
	private GraphNodeRiskMissingDefinitionPresentWhenRequiredValidator() {
	}

	@Override
	public List<GraphValidationResult> validate(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		
		final var results = new ArrayList<GraphValidationResult>();
		
		validateClasses(graph, results);
		validateOperations(graph, results);
		validateAttributes(graph, results);
		
		return results;
	}

	private static void validateClasses(final Graph graph, final List<GraphValidationResult> results) {
		final var removeClassNodes = graph.getNodes(GraphNodeMicrostepRemoveClass.class);
		for (final var removeClassNode : removeClassNodes) {
			final var classNodeRemoved = removeClassNode.getClassNode();
			// TODO find references, add validation result for each reference
		}
	}
	
	private static void validateOperations(final Graph graph, final List<GraphValidationResult> results) {
		final var removeMethodNodes = graph.getNodes(GraphNodeMicrostepRemoveMethod.class);
		for (final var removeMethodNode : removeMethodNodes) {
			final var operationNodeRemoved = removeMethodNode.getOperationNode();
			final var invocations =
				operationNodeRemoved
					.getInvocations()
					.stream()
					.map(GraphNodeCode.class::cast)
					.collect(Collectors.toUnmodifiableSet());
			final var validationResult =
				new GraphNodeRiskMissingDefinitionPresentWhenRequiredValidationResult(
					graph,
					invocations,
					operationNodeRemoved,
					false
				);
			results.add(validationResult);
		}
	}
	
	private static void validateAttributes(final Graph graph, final List<GraphValidationResult> results) {
		final var removeFieldNodes = graph.getNodes(GraphNodeMicrostepRemoveField.class);
		for (final var removeFieldNode : removeFieldNodes) {
			// TODO find references, add validation result for each reference
		}
	}
}