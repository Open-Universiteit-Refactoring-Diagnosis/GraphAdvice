package nl.ou.refactoring.advice.edges;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Represents an edge from one node to another in a Refactoring Advice Graph.
 */
public abstract class GraphEdge {
	private final UUID id;
	private final GraphNode sourceNode;
	private final GraphNode destinationNode;
	
	/**
	 * Initialises a new instance of {@link GraphEdge}.
	 * @param sourceNode The source node.
	 * @param destinationNode The destination node.
	 * @throws ArgumentNullException Thrown if source or destination is null.
	 */
	protected GraphEdge(GraphNode sourceNode, GraphNode destinationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(sourceNode, "source");
		ArgumentGuard.requireNotNull(destinationNode, "destination");
		this.id = UUID.randomUUID();
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
	}
	
	/**
	 * Gets the unique identifier of the edge.
	 * @return The unique identifier of the edge.
	 */
	public UUID getId() {
		return this.id;
	}
	
	/**
	 * Gets the source node of this edge.
	 * @return The source node of this edge.
	 */
	public GraphNode getSourceNode() {
		return this.sourceNode;
	}
	
	/**
	 * Gets the destination node of this edge.
	 * @return The destination node of this edge.
	 */
	public GraphNode getDestinationNode() {
		return this.destinationNode;
	}
	
	/**
	 * Gets the label for the edge.
	 * @return The label for the edge.
	 */
	public String getLabel() {
		return ResourceProvider.GraphEdgeLabels.getLabel(this.getClass());
	}
	
	private GraphEdge createClone(GraphNode source, GraphNode destination)
			throws
				GraphEdgeCloneConstructorNotFoundException,
				GraphEdgeCloneFailedException
	{
		try {
			return
				(GraphEdge)List.of(this.getClass().getConstructors())
					.stream()
					.filter(c -> {
						final var parameters = c.getParameters();
						return
							parameters[0].getType().isAssignableFrom(source.getClass()) &&
							parameters[1].getType().isAssignableFrom(destination.getClass());
					})
					.findAny()
					.orElseThrow()
					.newInstance(source, destination);
		} catch (NoSuchElementException ex) {
			throw new GraphEdgeCloneConstructorNotFoundException(this.getClass());
		} catch (Exception ex) {
			throw new GraphEdgeCloneFailedException(this.getClass(), ex);
		}
	}
	
	/**
	 * Creates a clone of this edge with the specified source and destination nodes.
	 * @param source The source node for the cloned edge.
	 * @param destination The destination node for the cloned edge.
	 * @return The cloned edge.
	 * @throws GraphEdgeNodesNotClonedException Thrown if the specified source and destination nodes are not clones of their original.
	 */
	public GraphEdge clone(GraphNode source, GraphNode destination)
			throws
				GraphEdgeCloneConstructorNotFoundException,
				GraphEdgeCloneFailedException,
				GraphEdgeNodesNotClonedException
	{
		final var sourceOriginal = this.getSourceNode();
		final var destinationOriginal = this.getDestinationNode();
		if (source == sourceOriginal ||
				destination == destinationOriginal ||
				source.getGraph() == sourceOriginal.getGraph() ||
				destination.getGraph() == destinationOriginal.getGraph()) {
			throw new GraphEdgeNodesNotClonedException
			(
				sourceOriginal,
				destinationOriginal,
				source,
				destination
			);
		}
		return createClone(source, destination);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (this.getClass() != other.getClass()) {
			return false;
		}
		return this.equals((GraphEdge)other);
	}
	
	/**
	 * Determines whether this edge and the other edge are equal.
	 * @param other The other edge to compare.
	 * @return True if the edges are equal (source and destination nodes are the same), otherwise false.
	 */
	public boolean equals(GraphEdge other) {
		if (other == null) {
			return false;
		}
		return this.sourceNode.equals(other.getSourceNode()) && this.destinationNode.equals(other.getDestinationNode());
	}
}
