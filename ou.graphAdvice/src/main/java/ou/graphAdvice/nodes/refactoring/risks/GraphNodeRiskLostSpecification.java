package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.refactoring.GraphEdgeAffects;
import ou.graphAdvice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Lost Specification" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that specifies the root of a tree of overriding methods is removed.
 */
public final class GraphNodeRiskLostSpecification extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskLostSpecification}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskLostSpecification(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Lost Specification" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Lost Specification" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.addEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
