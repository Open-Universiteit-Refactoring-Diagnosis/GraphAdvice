package nl.ou.refactoring.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A path in a graph.
 */
public final class GraphPath implements Cloneable {
	private final List<GraphPathSegment> segments;

	/**
	 * Initialises a new instance of {@link GraphPath}.
	 * @param startNode The start node of the path.
	 * @throws ArgumentNullException Thrown if start is null.
	 */
	public GraphPath(GraphNode startNode)
			throws NullPointerException {
		this.segments = new ArrayList<GraphPathSegment>();
		this.segments.add(new GraphPathSegment(null, startNode));
	}
	
	/**
	 * Appends a new segment to the path.
	 * @param edge The edge of the segment.
	 * @param node The node of the segment.
	 * @return The new segment of the path.
	 * @throws NullPointerException Thrown if edge or node is null.
	 * @throws GraphPathSegmentInvalidException Thrown if the segment is invalid. (i.e. the last node in the path does not support the edge in the new segment)
	 */
	public GraphPathSegment append(GraphEdge edge, GraphNode node)
			throws NullPointerException, GraphPathSegmentInvalidException {
		Objects.requireNonNull(edge, "edge");
		Objects.requireNonNull(node, "node");
		final var lastNode = this.segments.getLast().getNode();
		if (!lastNode.getEdges().contains(edge) ||
				edge.getDestinationNode() != node) {
			throw new GraphPathSegmentInvalidException(node, edge);
		}
		final var segment = new GraphPathSegment(edge, node);
		this.segments.addLast(segment);
		return segment;
	}
	
	/**
	 * Determines whether the specified node is in the path.
	 * @param node The node to check whether it is in the path.
	 * @return True if the node is in the path, otherwise false.
	 */
	public boolean contains(GraphNode node) {
		if (node == null) {
			return false;
		}
		return
				this
					.segments
					.stream()
					.anyMatch(segment -> segment.getNode().equals(node));
	}
	
	/**
	 * Returns all nodes on the path that are an instance of classType 
	 * or an instance of a specialisation of classType.
	 * @param <TNode> The type of nodes to look for.
	 * @param classType The type of nodes to look for.
	 * @return An unmodifiable list of nodes that are an instance of classType or an instance of a specialisation of classType, in the same order as the path.
	 * @throws NullPointerException Thrown if classType is null.
	 */
	public <TNode extends GraphNode> List<TNode> getNodes(Class<TNode> classType)
			throws NullPointerException {
		Objects.requireNonNull(classType, "classType");
		return
				Collections.unmodifiableList(
					this
						.segments
						.stream()
						.map(segment -> segment.getNode())
						.filter(node -> classType.isAssignableFrom(node.getClass()))
						.map(classType::cast)
						.toList()
				);
	}
	
	/**
	 * Gets the segments of the path.<br />
	 * <strong>Note:</strong> the returned list cannot be modified.
	 * @return The segments of the path.
	 */
	public List<GraphPathSegment> getSegments() {
		return Collections.unmodifiableList(this.segments);
	}
	
	@Override
	public Object clone() {
		final var first = this.segments.get(0).getNode();
		final var path = new GraphPath(first);
		for (var i = 1; i < this.segments.size(); i++) {
			final var segment = this.segments.get(i);
			try {
				path.append(segment.getEdge(), segment.getNode());
			} catch (GraphPathSegmentInvalidException e) {
				e.printStackTrace();
			}
		}
		return path;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		return this.equals((GraphPath)other);
	}
	
	/**
	 * Determines whether both {@link GraphPath} instances are equal.
	 * @param other The other graph path to compare with.
	 * @return True if the paths are equal, otherwise false.
	 */
	public boolean equals(GraphPath other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (this.segments.size() != other.segments.size()) {
			return false;
		}
		return
				IntStream
					.range(0, this.segments.size())
					.parallel()
					.allMatch(index -> this.segments.get(index).equals(other.segments.get(index)));
	}
}
