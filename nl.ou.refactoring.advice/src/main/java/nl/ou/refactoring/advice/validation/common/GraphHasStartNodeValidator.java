package nl.ou.refactoring.advice.validation.common;

import java.util.Collections;
import java.util.List;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.validation.GraphValidationResult;
import nl.ou.refactoring.advice.validation.GraphValidator;

/**
 * Validates that the {@link Graph} has a start node.
 */
public final class GraphHasStartNodeValidator implements GraphValidator {
	/**
	 * The singleton instance of {@link GraphHasStartNodeValidator}.
	 */
	public static final GraphHasStartNodeValidator INSTANCE = new GraphHasStartNodeValidator();
	
	/**
	 * Initialises a new instance of {@link GraphHasStartNodeValidator}.
	 */
	private GraphHasStartNodeValidator() {
	}

	@Override
	public List<GraphValidationResult> validate(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		final var startNode = graph.getStart();
		return
			Collections.unmodifiableList(
				List.of(new GraphHasStartNodeValidationResult(graph, startNode.isPresent()))
			);
	}
}
