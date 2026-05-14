package nl.ou.refactoring.advice.validation;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A validation result after validating a Refactoring Advice Graph {@link Graph}.
 */
public abstract class GraphValidationResult {
	private final Graph graph;
	private final boolean isValid;

	/**
	 * Initialises a new instance of {@link GraphValidationResult}.
	 * @param graph The {@link Graph} under validation.
	 * @param isValid Indicates whether the {@link Graph} is valid.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphValidationResult(Graph graph, boolean isValid)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.graph = graph;
		this.isValid = isValid;
	}

	/**
	 * Gets the {@link Graph} under validation.
	 * @return The {@link Graph} under validation.
	 */
	public final Graph getGraph() {
		return this.graph;
	}
	
	/**
	 * Gets a value that indicates whether the {@link Graph} is valid.
	 * @return A value that indicates whether the {@link Graph} is valid.
	 */
	public final boolean getIsValid() {
		return this.isValid;
	}
}