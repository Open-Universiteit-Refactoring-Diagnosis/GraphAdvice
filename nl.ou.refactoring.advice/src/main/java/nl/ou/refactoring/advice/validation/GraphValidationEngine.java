package nl.ou.refactoring.advice.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * An engine that validates Refactoring Advice Graph (RAGs) {@link Graph}.
 */
public final class GraphValidationEngine {
	private final Set<GraphValidator> validators; 
	
	/**
	 * Initialises a new instance of {@link GraphValidatorEngine}.
	 */
	public GraphValidationEngine() {
		this.validators = new HashSet<>();
	}
	
	/**
	 * Adds a validator that will participate in validation of a Refactoring Advice Graph (RAG) {@link Graph}.
	 * @param validator Validates Refactoring Advice Graph (RAG) {@link Graph}.
	 * @throws ArgumentNullException Thrown if validator is null.
	 */
	public void addValidator(GraphValidator validator) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(validator, "validator");
		this.validators.add(validator);
	}

	/**
	 * Validates a Refactoring Advice Graph (RAG) {@link graph}.
	 * @param graph A Refactoring Advice Graph (RAG) {@link graph}.
	 * @return An unmodifiable list of validation results {@link GraphValidationResult}.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public List<GraphValidationResult> validate(Graph graph)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		final var validationResults = new ArrayList<GraphValidationResult>();
		for (final var validator : this.validators) {
			validationResults.addAll(validator.validate(graph));
		}
		return Collections.unmodifiableList(validationResults);
	}
}