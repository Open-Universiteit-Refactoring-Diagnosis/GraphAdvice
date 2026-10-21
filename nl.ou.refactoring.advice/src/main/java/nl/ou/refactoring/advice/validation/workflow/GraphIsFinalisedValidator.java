package nl.ou.refactoring.advice.validation.workflow;

import java.util.List;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates that the {@link Graph} is finalised.
 */
public final class GraphIsFinalisedValidator implements GraphValidator {
	/**
	 * The singleton instance of {@link GraphIsFinalisedValidator}.
	 */
	public static final GraphIsFinalisedValidator INSTANCE = new GraphIsFinalisedValidator();
	
	/**
	 * Initialises a new instance of {@link GraphIsFinalisedValidator}.
	 */
	private GraphIsFinalisedValidator() {
	}

	@Override
	public List<GraphValidationResult> validate(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return List.of(new GraphIsFinalisedValidationResult(graph, graph.isFinalised()));
	}
}