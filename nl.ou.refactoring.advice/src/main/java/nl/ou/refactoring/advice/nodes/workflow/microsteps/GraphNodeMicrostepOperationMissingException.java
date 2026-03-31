package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if no operation node is associated with a node that represents a microstep that manipulates an operation.
 */
public final class GraphNodeMicrostepOperationMissingException extends GraphValidationException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 6125098627208117981L;
	
	/**
	 * The delinquent microstep node.
	 */
	private final GraphNodeMicrostep microstepNode;

	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepOperationMissingException}.
	 * @param microstepNode The delinquent microstep node.
	 */
	public GraphNodeMicrostepOperationMissingException(GraphNodeMicrostep microstepNode) {
		this.microstepNode = microstepNode;
	}
	
	/**
	 * Gets the delinquent microstep node.
	 * @return {@link GraphNodeMicrostep} The delinquent microstep node.
	 */
	public GraphNodeMicrostep getMicrostepNode() {
		return this.microstepNode;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(GraphNodeMicrostepOperationMissingException.class);
		return String.format(messageTemplate, this.microstepNode.getId());
	}
}