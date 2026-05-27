package nl.ou.refactoring.advice.nodes.workflow.microsteps.validation;

import java.util.ArrayList;
import java.util.List;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.risks.validation.GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates whether the Add Method microstep has all the necessary nodes
 * associated with it.
 */
public final class GraphNodeMicrostepAddMethodValidator implements GraphValidator {
	/**
	 * The singleton instance of {@link GraphNodeMicrostepAddMethodValidator}.
	 */
	public static GraphNodeMicrostepAddMethodValidator INSTANCE = new GraphNodeMicrostepAddMethodValidator();

	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddMethodValidator}.
	 */
	private GraphNodeMicrostepAddMethodValidator() {
	}

	@Override
	public List<GraphValidationResult> validate(final Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");

		final var results = new ArrayList<GraphValidationResult>();

		validateRiskDoubleDefinition(graph, results);

		return results;
	}

	private static void validateRiskDoubleDefinition(final Graph graph, final List<GraphValidationResult> results) {
		results.addAll(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator.INSTANCE.validate(graph));
	}
}
