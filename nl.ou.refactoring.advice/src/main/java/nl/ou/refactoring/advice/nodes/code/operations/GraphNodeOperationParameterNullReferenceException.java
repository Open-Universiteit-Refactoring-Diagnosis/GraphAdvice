package nl.ou.refactoring.advice.nodes.code.operations;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a reference to a node that represents an operation parameter is null.
 */
public final class GraphNodeOperationParameterNullReferenceException extends GraphValidationException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 4306978177264319202L;
	
	/**
	 * The node that represents the operation to which the operation parameter belongs.
	 */
	private final GraphNodeOperation operationNode;
	
	/**
	 * The index of the operation parameter.
	 */
	private final int index;
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperationParameterNullReferenceException}.
	 * @param operationNode The node that represents the operation to which the operation parameter belongs.
	 * @param index The index of the operation parameter.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 * @throws IllegalArgumentException Thrown if index is not greater than or equal to 0.
	 */
	public GraphNodeOperationParameterNullReferenceException(GraphNodeOperation operationNode, int index)
			throws ArgumentNullException, IllegalArgumentException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		ArgumentGuard.requireGreaterThanOrEqual(0, index, "index");
		this.operationNode = operationNode;
		this.index = index;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(this.getClass());
		return String.format(messageTemplate, this.operationNode.getOperationName(), index);
	}
	
	/**
	 * Gets the node that represents the operation that the operation parameter belongs to.
	 * @return The node that represents the operation that the operation parameter belongs to.
	 */
	public GraphNodeOperation getOperationNode() {
		return this.operationNode;
	}
	
	/**
	 * Gets the index of the operation parameter.
	 * @return The index of the operation parameter.
	 */
	public int getIndex() {
		return this.index;
	}
}