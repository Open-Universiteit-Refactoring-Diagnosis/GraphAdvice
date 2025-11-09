package nl.ou.refactoring.advice.io.layouts.globalRanking;

import nl.ou.refactoring.advice.GraphPathSegment;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A segment in a graph variation.
 */
public final class GraphVariationSegment {
	private final Class<? extends GraphEdge> edgeClass;
	private final Class<? extends GraphNode> nodeClass;

	/**
	 * Initialises a new instance of {@link GraphVariationSegment}.
	 * @param edgeClass The class of incoming edge in the segment.
	 * @param nodeClass The class of node in the segment.
	 * @throws ArgumentNullException Thrown if nodeClass is null.
	 */
	public GraphVariationSegment(
			Class<? extends GraphEdge> edgeClass,
			Class<? extends GraphNode> nodeClass)
					throws ArgumentNullException {
		ArgumentGuard.requireNotNull(nodeClass, "nodeClass");
		this.edgeClass = edgeClass;
		this.nodeClass = nodeClass;
	}
	
	/**
	 * Gets the class of the incoming edge in the segment.
	 * @return The class of the incoming edge in the segment. Can be null.
	 */
	public Class<? extends GraphEdge> getEdgeClass() {
		return this.edgeClass;
	}
	
	/**
	 * Gets the class of the node in the segment.
	 * @return The class of the node in the segment.
	 */
	public Class<? extends GraphNode> getNodeClass() {
		return this.nodeClass;
	}
	
	/**
	 * Creates a {@link GraphVariationSegment} from a {@link GraphPathSegment}.
	 * @param segment The graph path segment to create a graph variation segment from.
	 * @return The newly created {@link GraphVariationSegment}.
	 * @throws ArgumentNullException Thrown if segment is null.
	 */
	public static GraphVariationSegment from(GraphPathSegment segment)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(segment, "segment");
		final var edge = segment.getEdge();
		final var node = segment.getNode();
		return
				edge == null
					? new GraphVariationSegment(null, node.getClass())
					: new GraphVariationSegment(edge.getClass(), node.getClass());
	}
}
