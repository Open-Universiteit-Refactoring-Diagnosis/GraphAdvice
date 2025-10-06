package nl.ou.refactoring.advice.nodes.refactoring.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.refactoring.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Missing Abstract Implementation" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that is removed implements an abstract method and no other overrides exist.
 */
public final class GraphNodeRiskMissingAbstractImplementation extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingAbstractImplementation}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingAbstractImplementation(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Missing Abstract Implementation" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Missing Abstract Implementation" risk and the affected Operation.
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
