package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeAffects;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeCauses;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.mechanics.GraphNodeMechanic;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostep;

/**
 * Represents a risk in a Refactoring Advice Graph.
 */
public abstract class GraphNodeDanger extends GraphNodeMechanic {
	/**
	 * Initialises a new instance of {@link GraphNodeDanger}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeDanger(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Gets the nodes that are affected by the risk.
	 * 
	 * @return The nodes that are affected by the risk.
	 */
	public Set<GraphNode> getAffected() {
		return this.getEdges(GraphEdgeAffects.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.collect(Collectors.toSet());
	}

	/**
	 * Gets the node that causes the risk.
	 * 
	 * @return The node that causes the risk, wrapped in {@link Optional}, or an
	 *         empty {@link Optional} if not found.
	 */
	public Optional<GraphNodeMicrostep> getCause() {
		return this.getEdgesIncoming(GraphEdgeCauses.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeMicrostep.class::isInstance)
				.map(GraphNodeMicrostep.class::cast)
				.findAny();
	}

	@Override
	public String getLabel() {
		return "Danger";
	}
}
