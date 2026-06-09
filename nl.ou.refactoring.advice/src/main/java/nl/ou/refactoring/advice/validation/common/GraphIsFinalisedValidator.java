package nl.ou.refactoring.advice.validation.common;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
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
		final var finalisingNodes =
			graph
				.getNodes(GraphNodeMicrostep.class)
				.stream()
				.filter((node) -> node.getEdges(GraphEdgeFinalises.class).size() == 1)
				.collect(Collectors.toUnmodifiableSet());
		return
			Collections.unmodifiableList(
				List.of(new GraphIsFinalisedValidationResult(graph, finalisingNodes.size() == 1))
			);
	}

}
