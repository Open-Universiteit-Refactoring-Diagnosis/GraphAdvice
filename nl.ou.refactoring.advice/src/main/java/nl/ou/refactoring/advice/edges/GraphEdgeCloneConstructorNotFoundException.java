package nl.ou.refactoring.advice.edges;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a valid constructor on an edge could not be found
 * for the purpose of cloning.
 */
public final class GraphEdgeCloneConstructorNotFoundException extends RuntimeException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -1212772316064643285L;
	
	/**
	 * The class type of the edge of which a valid constructor for cloning could not be found.
	 */
	private final Class<? extends GraphEdge> edgeClassType;

	/**
	 * Initialises a new instance of {@link GraphEdgeCloneConstructorNotFoundException}.
	 * @param edgeClassType The class type of the edge of which a valid constructor for cloning could not be found.
	 * @throws ArgumentNullException Thrown if edgeClassType is null.
	 */
	public GraphEdgeCloneConstructorNotFoundException(Class<? extends GraphEdge> edgeClassType)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(edgeClassType, "edgeClassType");
		this.edgeClassType = edgeClassType;
	}
	
	/**
	 * Gets the class type of the edge of which a valid constructor for cloning could not be found.
	 * @return The class type of the edge of which a valid constructor for cloning could not be found.
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