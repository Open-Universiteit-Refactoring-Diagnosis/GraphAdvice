package nl.ou.refactoring.advice.validation;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A validation result after validating a Refactoring Advice Graph {@link Graph} that can be fixed immediately.
 */
public abstract class GraphValidationFixableResult extends GraphValidationResult {
	/**
	 * Initialises a new instance of {@link GraphValidationFixableResult}.
	 * @param graph The Refactoring Advice Graph (RAG) {@link Graph} under validation.
	 * @param isValid A value that indicates whether the Refactoring Advice Graph (RAG) {@link Graph} is valid.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphValidationFixableResult(Graph graph, boolean isValid) throws ArgumentNullException {
		super(graph, isValid);
	}

	/**
	 * Fixes the validation result.
	 * @param cloneGraph A value that indicates whether the fix should be in a cloned {@link Graph}.
	 * @return A Refactoring Advice Graph (RAG) with the fix of the validation result. A clone if <pre>{@code cloneGraph == true}</pre>.
	 * @throws GraphValidationResultFixFailedException Thrown if the automated fix fails.
	 */
	public abstract Graph fix(boolean cloneGraph) throws GraphValidationResultFixFailedException;
}