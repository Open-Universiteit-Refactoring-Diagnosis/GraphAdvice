package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Missing Super Implementation" risk in a Refactoring Advice Graph.
 * This risk may arise if an overriding method is removed and none of the subclasses contain an alternative implementation.
 */
public final class GraphNodeRiskMissingSuperImplementation extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingSuperImplementation}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingSuperImplementation(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the "Missing Super Implementation" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Missing Super Implementation" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
