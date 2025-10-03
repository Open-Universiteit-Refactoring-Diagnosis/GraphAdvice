package ou.graphAdvice.edges;

import java.util.UUID;

import ou.graphAdvice.contracts.ArgumentGuard;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.nodes.GraphNode;

/**
 * Represents an edge from one node to another in a Refactoring Advice Graph.
 */
public abstract class GraphEdge {
	private final UUID id;
	private final GraphNode source;
	private final GraphNode destination;
	
	/**
	 * Initialises a new instance of {@link GraphEdge}.
	 * @param source The source node.
	 * @param destination The destination node.
	 * @throws ArgumentNullException Thrown if source or destination is null.
	 */
	protected GraphEdge(GraphNode source, GraphNode destination)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(source, "source");
		ArgumentGuard.requireNotNull(destination, "destination");
		this.id = UUID.randomUUID();
		this.source = source;
		this.destination = destination;
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
	public GraphNode getSource() {
		return this.source;
	}
	
	/**
	 * Gets the destination node of this edge.
	 * @return The destination node of this edge.
	 */
	public GraphNode getDestination() {
		return this.destination;
	}
}
