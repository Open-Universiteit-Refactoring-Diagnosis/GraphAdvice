package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Missing Definition" risk in a Refactoring Advice Graph.
 * This risk may arise if a method is still called after it has been (re)moved.
 */
public final class GraphNodeRiskMissingDefinition extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingDefinition}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingDefinition(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Missing Definition" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Missing Definition" risk and the affected Operation.
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
