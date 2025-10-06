package nl.ou.refactoring.advice.edges;

import java.util.UUID;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

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
	
	public boolean equals(GraphEdge other) {
		if (other == null) {
			return false;
		}
		return this.sourceNode.equals(other.getSourceNode()) && this.destinationNode.equals(other.getDestinationNode());
	}
}
