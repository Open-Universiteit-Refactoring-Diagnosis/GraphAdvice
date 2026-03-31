package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if no attribute node is associated with a node that represents a microstep that manipulates an attribute.
 */
public final class GraphNodeMicrostepAttributeMissingException extends GraphValidationException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 1606169629855104377L;
	
	/**
	 * The delinquent microstep node.
	 */
	private final GraphNodeMicrostep microstepNode;

	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAttributeMissingException}.
	 * @param microstepNode The delinquent microstep node.
	 */
	public GraphNodeMicrostepAttributeMissingException(GraphNodeMicrostep microstepNode) {
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
				.getMessageTemplate(GraphNodeMicrostepAttributeMissingException.class);
		return String.format(messageTemplate, this.microstepNode.getId());
	}
}