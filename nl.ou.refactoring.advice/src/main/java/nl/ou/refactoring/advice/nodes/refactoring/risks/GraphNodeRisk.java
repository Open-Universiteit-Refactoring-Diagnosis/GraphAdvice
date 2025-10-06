package nl.ou.refactoring.advice.nodes.refactoring.risks;

import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.refactoring.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.refactoring.GraphNodeRefactoring;

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
					.map(edge -> edge.getDestination())
					.collect(Collectors.toSet());
	}
}
