package nl.ou.refactoring.advice.edges.code.operations.expressions;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;

/**
 * An edge in a Refactoring Advice Graph that indicates an invocation of a method.
 */
public final class GraphEdgeInvokes extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeInvokes}.
	 * @param sourceNode The expression that invokes the operation.
	 * @param destinationNode The operation that is being invoked.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeInvokes(GraphNodeMethodInvocationExpression sourceNode, GraphNodeOperation destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Gets the method invocation expression node.
	 * @return {@link GraphNodeMethodInvocationExpression} The method invocation expression node.
	 */
	public GraphNodeMethodInvocationExpression getMethodInvocationExpression() {
		return (GraphNodeMethodInvocationExpression)this.getSourceNode();
	}
	
	/**
	 * Gets the node that represents the operation that is invoked.
	 * @return {@link GraphNodeOperation} The operation node.
	 */
	public GraphNodeOperation getOperationNode() {
		return (GraphNodeOperation)this.getDestinationNode();
	}
}