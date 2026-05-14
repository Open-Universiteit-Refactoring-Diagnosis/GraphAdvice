package nl.ou.refactoring.advice.validation;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Validates a particular aspect of a Refactoring Advice Graph (RAG) {@link Graph}.
 */
public interface GraphValidator {
	/**
	 * Validates the {@link Graph}.
	 * @param graph The Refactoring Advice Graph {@link Graph} to validate.
	 * @return The validation result.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	GraphValidationResult validate(Graph graph) throws ArgumentNullException;
}