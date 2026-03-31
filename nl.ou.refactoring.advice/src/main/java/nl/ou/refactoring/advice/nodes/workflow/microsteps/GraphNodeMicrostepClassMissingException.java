package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown  if no Class node is associated with a node that represents a microstep[ 
 */
public final class GraphNodeMicrostepClassMissingException extends GraphValidationException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -3927082807823439185L;
	
	/**
	 * The delinquent microstep node.
	 */
	private final GraphNodeMicrostep microstepNode;

	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepClassMissingException}.
	 * @param microstepNode The delinquent microstep node.
	 */
	public GraphNodeMicrostepClassMissingException(GraphNodeMicrostep microstepNode) {
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
				.getMessageTemplate(GraphNodeMicrostepClassMissingException.class);
		return String.format(messageTemplate, this.microstepNode.getId());
	}

}
