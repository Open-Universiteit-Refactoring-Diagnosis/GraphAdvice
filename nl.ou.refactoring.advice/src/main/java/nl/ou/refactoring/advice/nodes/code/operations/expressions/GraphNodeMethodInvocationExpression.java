package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeInvokes;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * A node in a Refactoring Advice Graph that represents a method invocation expression.
 */
public final class GraphNodeMethodInvocationExpression extends GraphNodeStatementExpression {
	/**
	 * Initialises a new instance of {@link GraphNodeMethodInvocationExpression}.
	 * @param graph The Refactoring Advice Graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMethodInvocationExpression(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the node that represents the operation that is invoked by the Method Invocation Expression.
	 * @return {@link GraphNodeOperation} The node that represents the operation that is invoked by the method invocation expression.
	 */
	public GraphNodeOperation getInvokedOperationNode() {
		return
			this
				.getEdges(GraphEdgeInvokes.class)
				.stream()
				.map(edge -> edge.getOperationNode())
				.findAny()
				.orElse(null);
	}
	
	/**
	 * Indicates that the method invocation expression represented by this node invokes the operation represented by the {@link GraphNodeOperation} operationNode.
	 * @param operationNode The node that represents the operation that is invoked by the method invocation expression that is represented by this node.
	 * @return An edge that indicates that the method invocation expression invokes the operation.
	 */
	public GraphEdgeInvokes invokes(GraphNodeOperation operationNode) {
		ArgumentGuard.requireNotNull(operationNode, "operation");
		return
			this
				.graph
				.getOrAddEdge(
					this,
					operationNode,
					(sourceNode, destinationNode) -> new GraphEdgeInvokes(sourceNode, destinationNode),
					GraphEdgeInvokes.class
				);
	}
}
