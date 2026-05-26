package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a {@link GraphNodeMethodInvocationExpression} is missing an Operation node.
 */
public final class GraphNodeMethodInvocationExpressionOperationNodeMissingException extends GraphValidationException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 2431643160818944769L;
	
	private final GraphNodeMethodInvocationExpression expressionNode;

	/**
	 * Initialises a new instance of {@link GraphNodeMethodInvocationExpression}.
	 * @param expressionNode The Method Invocation Expression node that is missing an Operation node.
	 */
	public GraphNodeMethodInvocationExpressionOperationNodeMissingException(
		GraphNodeMethodInvocationExpression expressionNode
	) {
		ArgumentGuard.requireNotNull(expressionNode, "expressionNode");
		this.expressionNode = expressionNode;
	}
	
	/**
	 * Gets the Method Invocation Expression node that is missing an operation node.
	 * @return The Method Invocation Expression node that is missing an operation node.
	 */
	public GraphNodeMethodInvocationExpression getExpressionNode() {
		return this.expressionNode;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(
					GraphNodeMethodInvocationExpressionOperationNodeMissingException.class
				);
		return messageTemplate;
	}

}
