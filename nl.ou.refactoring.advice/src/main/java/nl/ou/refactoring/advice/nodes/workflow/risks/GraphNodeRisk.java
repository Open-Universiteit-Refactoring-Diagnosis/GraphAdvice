package nl.ou.refactoring.advice.nodes.workflow.risks;

import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoring;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * Represents a risk in a Refactoring Advice Graph.
 */
public abstract class GraphNodeRisk extends GraphNodeRefactoring {
	/**
	 * Initialises a new instance of {@link GraphNodeRisk}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRisk(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the nodes that are affected by the risk.
	 * @return The nodes that are affected by the risk.
	 */
	public Set<GraphNode> getAffected() {
		return
				this
					.graph
					.getEdgesFrom(this, GraphEdgeAffects.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.collect(Collectors.toSet());
	}
	
	/**
	 * Gets the nodes that neutralise the risk.
	 * @return The nodes that neutralise the risk.
	 */
	public Set<GraphNode> getNeutralisers() {
		final var neutralisers =
				this
					.graph
					.getEdgesTo(this, GraphEdgeObsolesces.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.collect(Collectors.toSet());
		if (neutralisers.size() <= 1) {
			return neutralisers;
		}
		
		// Ensure a chain of microsteps in the correct order.
		final var neutraliserMicrosteps =
				neutralisers
					.stream()
					.filter(GraphNodeMicrostep.class::isInstance)
					.map(GraphNodeMicrostep.class::cast)
					.sorted((one, other) -> one.getPrecedingLength(other))
					.collect(Collectors.toList());
		if (neutraliserMicrosteps.size() <= 1) {
			return neutralisers;
		}
		for (var i = neutraliserMicrosteps.size() - 1; i > 0; i--) {
			final var current = neutraliserMicrosteps.get(i);
			final var previous = neutraliserMicrosteps.get(i - 1);
			if (!current.isPrecededBy(previous)) {
				neutralisers.remove(previous);
			}
		}
		return neutralisers;
	}
}
