package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Preceding Overload" risk in a Refactoring Advice Graph.
 * This risk may arise if a new method overloads an existing method and precedes the overloaded method in terms of its parameters.
 */
public final class GraphNodeRiskPrecedingOverload extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskPrecedingOverload}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskPrecedingOverload(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Preceding Overload" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Preceding Overload" risk and the affected Operation.
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