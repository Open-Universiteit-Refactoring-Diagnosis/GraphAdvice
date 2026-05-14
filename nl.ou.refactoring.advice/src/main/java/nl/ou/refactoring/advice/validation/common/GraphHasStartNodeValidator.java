package nl.ou.refactoring.advice.validation.common;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates that the {@link Graph} has a start node.
 */
public final class GraphHasStartNodeValidator implements GraphValidator {
	public static final GraphHasStartNodeValidator INSTANCE = new GraphHasStartNodeValidator();
	
	/**
	 * Initialises a new instance of {@link GraphHasStartNodeValidator}.
	 */
	private GraphHasStartNodeValidator() {
	}

	@Override
	public GraphValidationResult validate(Graph graph) throws ArgumentNullException {
		final var startNode = graph.getStart();
		return new GraphHasStartNodeValidationResult(graph, startNode.isPresent());
	}
}
