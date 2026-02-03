package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;

/**
 * Represents a "Changed Nested Relationship" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that is added will cause callers in an inner class
 * to call the method in the inner class instead.
 */
public class GraphNodeRiskChangedNestedRelationship extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskChangedNestedRelationship}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskChangedNestedRelationship(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Changed Nested Relationship" risk affects {@link GraphNodeOperation} operationNode.
	 * @param operationNode The {@link GraphNodeOperation} node that represents the operation affected by this risk.
	 * @return {@link GraphEdgeAffects} The edge that indicates that the Operation is affected by this risk.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		return
			this
				.graph
				.getOrAddEdge(
					this,
					operationNode,
					(sourceNode, destinationNode) -> new GraphEdgeAffects(sourceNode, destinationNode),
					GraphEdgeAffects.class
				);
	}
	
	/**
	 * Indicates that the "Changed Nested Relationship" risk
	 * affects {@link GraphNodeMethodInvocationExpressionNode} methodInvocationExpressionNode.
	 * @param methodInvocationExpressionNode The {@link GraphNodeMethodInvocationExpression} node that represents the Method Invocation Expression affected by this risk.
	 * @return {@link GraphEdgeAffects} The edge that indicates that the Method Invocation Expression is affected by this risk.
	 * @throws ArgumentNullException Thrown if methodInvocationExpressionNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeMethodInvocationExpression methodInvocationExpressionNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(methodInvocationExpressionNode, "methodInvocationExpressionNode");
		return
			this
				.graph
				.getOrAddEdge(
					this,
					methodInvocationExpressionNode,
					(sourceNode, destinationNode) -> new GraphEdgeAffects(sourceNode, destinationNode),
					GraphEdgeAffects.class
				);
	}
}