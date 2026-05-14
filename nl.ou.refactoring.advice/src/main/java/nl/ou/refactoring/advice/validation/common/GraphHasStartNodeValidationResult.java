package nl.ou.refactoring.advice.validation.common;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.validation.GraphValidationResult;

/**
 * A validation result that indicates whether the {@link Graph} under validation has a start node.
 */
public final class GraphHasStartNodeValidationResult extends GraphValidationResult {
	/**
	 * Initialises a new instance of {@link GraphHasStartNodeValidationResult}.
	 * @param graph The Refactoring Advice Graph {@link Graph} under validation.
	 * @param isValid A value that indicates whether the Refactoring Advice Graph {@link Graph} is valid.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphHasStartNodeValidationResult(Graph graph, boolean isValid) throws ArgumentNullException {
		super(graph, isValid);
	}
}