package nl.ou.refactoring.advice;

import java.util.Objects;

import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A segment in a path from a graph.
 */
public final class GraphPathSegment {
	private final GraphNode node;
	private final GraphEdge edge;

	/**
	 * Initialises a new instance of {@link GraphPathSegment}.
	 * @param edge An outgoing edge of the node.
	 * @param node The node of a graph.
	 * @throws NullPointerException Thrown if node is null. Edge can be null.
	 */
	public GraphPathSegment(GraphEdge edge, GraphNode node)
			throws NullPointerException {
		Objects.requireNonNull(node, "node");
		this.node = node;
		this.edge = edge;
	}
	
	/**
	 * Gets the incoming edge.
	 * @return The incoming edge.
	 */
	public GraphEdge getEdge() {
		return this.edge;
	}
	
	/**
	 * Gets the node of the segment.
	 * @return The node of the segment.
	 */
	public GraphNode getNode() {
		return this.node;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		return this.equals((GraphPathSegment)other);
	}
	
	/**
	 * Determines whether the two instances of {@link GraphPathSegment} are equal.
	 * @param other The other segment to compare with.
	 * @return True if the instances are equal, false if not.
	 */
	public boolean equals(GraphPathSegment other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		return
				this.getNode().equals(other.getNode()) &&
				((this.getEdge() == null && other.getEdge() == null) ||
				this.getEdge().equals(other.getEdge()));
	}
}
