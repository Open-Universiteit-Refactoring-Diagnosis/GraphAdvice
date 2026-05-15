package nl.ou.refactoring.advice.validation.common;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.validation.GraphValidationResult;

/**
 * A validation result that indicates whether the {@link Graph} under validation is finalised.
 */
public final class GraphIsFinalisedValidationResult extends GraphValidationResult {
	/**
	 * Initialises a new instance of {@link GraphIsFinalisedValidationResult}.
	 * @param graph The Refactoring Advice Graph (RAG) {@link Graph} under validation.
	 * @param isValid A value that indicates whether the Refactoring Advice Graph (RAG) {@link Graph} is valid.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphIsFinalisedValidationResult(Graph graph, boolean isValid) throws ArgumentNullException {
		super(graph, isValid);
	}
}