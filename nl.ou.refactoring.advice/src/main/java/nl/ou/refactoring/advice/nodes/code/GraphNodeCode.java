package nl.ou.refactoring.advice.nodes.code;

import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * Represents a node in a Refactoring Advice Graph that describes the affected programme code.
 */
public abstract class GraphNodeCode extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphNodeCode}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeCode(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets all {@link GraphNodeRisk} nodes associated with this code element.
	 * @return The {@link GraphNodeRisk} nodes associated with this code element.
	 */
	public Set<GraphNodeRisk> getRisks() {
		return
				this
					.getEdgesIncoming(GraphEdgeAffects.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.filter(node -> node instanceof GraphNodeRisk)
					.map(GraphNodeRisk.class::cast)
					.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets all {@link GraphNodeRisk} nodes associated with this code element, which are not neutralised.
	 * @return The {@link GraphNodeDanger} nodes associated with this code element.
	 */
	public Set<GraphNodeRisk> getDangers() {
		return
				this
					.getRisks()
					.stream()
					.filter(node -> node.getNeutralisers().isEmpty())
					.collect(Collectors.toUnmodifiableSet());
	}
}
