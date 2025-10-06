package nl.ou.refactoring.advice.nodes.workflow.risks;

import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoring;

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
		return
				this
					.graph
					.getEdgesTo(this, GraphEdgeObsolesces.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.collect(Collectors.toSet());
	}
}
