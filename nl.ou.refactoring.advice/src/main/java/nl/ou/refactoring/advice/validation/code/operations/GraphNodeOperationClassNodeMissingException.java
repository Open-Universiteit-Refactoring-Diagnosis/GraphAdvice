package nl.ou.refactoring.advice.validation.code.operations;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.GraphValidationException;

/**
 * An exception that is thrown if a {@link GraphNodeOperation} does not have an
 * associated class node when it is required.
 */
public final class GraphNodeOperationClassNodeMissingException extends GraphValidationException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -5288281321275796466L;

	/**
	 * The operation node that is missing an associated class node.
	 */
	private final GraphNodeOperation operationNode;

	/**
	 * Initialises a new instance of
	 * {@link GraphNodeOperationClassNodeMissingException}.
	 * 
	 * @param operationNode The operation node that is missing an associated class
	 *                      node.
	 */
	public GraphNodeOperationClassNodeMissingException(GraphNodeOperation operationNode) {
		this.operationNode = operationNode;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages
						.getMessageTemplate(GraphNodeOperationClassNodeMissingException.class);
		return MessageFormat.format(messageTemplate, this.operationNode.getOperationName());
	}

	/**
	 * Gets the operation node that is missing an associated class node.
	 * 
	 * @return The operation node that is missing an associated class node.
	 */
	public GraphNodeOperation getOperationNode() {
		return this.operationNode;
	}

}