package nl.ou.refactoring.advice.edges;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if cloning a Refactoring Advice Graph edge failed.
 */
public final class GraphEdgeCloneFailedException extends RuntimeException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -4078421275390601110L;

	/**
	 * The class type of the edge that could not be cloned.
	 */
	private final Class<? extends GraphEdge> edgeClassType;
	
	/**
	 * Initialises a new instance of {@link GraphEdgeCloneFailedException}.
	 * @param edgeClassType The class type of the edge that could not be cloned.
	 * @param cause The cause of the clone failure.
	 * @throws ArgumentNullException Thrown if edgeClassType is null.
	 */
	public GraphEdgeCloneFailedException
	(
		Class<? extends GraphEdge> edgeClassType,
		Throwable cause
	)
		throws ArgumentNullException
	{
		super(cause);
		ArgumentGuard.requireNotNull(edgeClassType, "edgeClassType");
		this.edgeClassType = edgeClassType;
	}
	
	/**
	 * Gets the class type of the edge that could not be cloned.
	 * @return The class type of the edge that could not be cloned.
	 */
	public Class<? extends GraphEdge> getEdgeClassType() {
		return this.edgeClassType;
	}
	
	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(this.getClass());
		return String.format(messageTemplate, this.edgeClassType.getName());
	}
}