package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

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
	 * Indicates that the Missing Definition risk affects the attribute that is represented by the specified {@link GraphNodeAttribute} attribute node.
	 * @param attributeNode {@link GraphNodeAttribute} The node that represents the affected attribute.
	 * @return The edge that indicates that the Missing Definition risk affects an attribute.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
			this,
			attributeNode,
			(source, destination) -> new GraphEdgeAffects(source, destination),
			GraphEdgeAffects.class
		);
	}
	
	/**
	 * Indicates that the "Missing Definition" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Missing Definition" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
			this,
			operationNode,
			(source, destination) -> new GraphEdgeAffects(source, destination),
			GraphEdgeAffects.class
		);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeRiskMissingDefinition(graph);
	}
}