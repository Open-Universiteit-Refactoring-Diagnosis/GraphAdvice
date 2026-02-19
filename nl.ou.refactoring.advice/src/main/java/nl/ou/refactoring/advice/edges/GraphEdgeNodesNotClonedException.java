package nl.ou.refactoring.advice.edges;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if an edge is cloned while receiving nodes
 * that are not clones of the ones in the original edge.
 */
public final class GraphEdgeNodesNotClonedException extends GraphValidationException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 8102925781738144537L;
	
	private final GraphNode sourceOriginal;
	private final GraphNode destinationOriginal;
	private final GraphNode sourceCloned;
	private final GraphNode destinationCloned;
	
	/**
	 * Initialises a new instance of {@link GraphEdgeNodesNotClonedException}.
	 * @param sourceOriginal The original edge's source node.
	 * @param destinationOriginal The original edge's destination node.
	 * @param sourceCloned The cloned edge's source node.
	 * @param destinationCloned The other edge's destination node.
	 * @throws ArgumentNullException Thrown if any of the constructor's arguments is null.
	 */
	public GraphEdgeNodesNotClonedException(
			GraphNode sourceOriginal,
			GraphNode destinationOriginal,
			GraphNode sourceCloned,
			GraphNode destinationCloned) {
		ArgumentGuard.requireNotNull(sourceOriginal, "sourceOriginal");
		ArgumentGuard.requireNotNull(destinationOriginal, "destinationOriginal");
		ArgumentGuard.requireNotNull(sourceCloned, "sourceOther");
		ArgumentGuard.requireNotNull(destinationCloned, "destinationOther");
		this.sourceOriginal = sourceOriginal;
		this.destinationOriginal = destinationOriginal;
		this.sourceCloned = sourceCloned;
		this.destinationCloned = destinationCloned;
	}
	
	/**
	 * Gets the original edge's source node.
	 * @return The original edge's source node.
	 */
	public GraphNode getSourceOriginal() {
		return this.sourceOriginal;
	}
	
	/**
	 * Gets the original edge's destination node.
	 * @return The original edge's destination node.
	 */
	public GraphNode getDestinationOriginal() {
		return this.destinationOriginal;
	}
	
	/**
	 * Gets the other edge's source node.
	 * @return The other edge's source node.
	 */
	public GraphNode getSourceCloned() {
		return this.sourceCloned;
	}
	
	/**
	 * Gets the other edge's destination node.
	 * @return The other edge's destination node
	 */
	public GraphNode getDestinationCloned() {
		return this.destinationCloned;
	}

	@Override
	public String getLocalizedMessage() {
		return ResourceProvider.ExceptionMessages.getMessageTemplate(GraphEdgeNodesNotClonedException.class);
	}
}