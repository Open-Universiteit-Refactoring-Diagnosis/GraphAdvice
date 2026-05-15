package nl.ou.refactoring.advice.validation;

import java.util.List;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Validates a particular aspect of a Refactoring Advice Graph (RAG) {@link Graph}.
 */
public interface GraphValidator {
	/**
	 * Validates the {@link Graph}.
	 * @param graph The Refactoring Advice Graph {@link Graph} to validate.
	 * @return An unmodifiable list of validation results {@link GraphValidationResult}.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	List<GraphValidationResult> validate(Graph graph) throws ArgumentNullException;
}