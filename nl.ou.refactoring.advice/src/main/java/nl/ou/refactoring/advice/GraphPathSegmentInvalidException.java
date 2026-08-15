package nl.ou.refactoring.advice;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * An exception that is thrown if an attempt was made to add a segment to a
 * graph path that is not valid when appended to the previous segments of the
 * path.
 */
public final class GraphPathSegmentInvalidException extends GraphValidationException {
	/**
	 * Generates serial version unique identifier.
	 */
	private static final long serialVersionUID = -5763936417696992897L;

	/**
	 * The node that is invalid within the segment.
	 */
	private final GraphNode node;

	/**
	 * The edge that is invalid within the segment.
	 */
	private final GraphEdge edge;

	/**
	 * Initialises a new instance of {@link GraphPathSegmentInvalidException}.
	 * 
	 * @param node The node in the path that should contain the edge.
	 * @param edge The edge that is invalid with the node.
	 * @throws ArgumentNullException Thrown if node or edge is null.
	 */
	public GraphPathSegmentInvalidException(GraphNode node, GraphEdge edge) throws ArgumentNullException {
		super();
		ArgumentGuard.requireNotNull(node, "node");
		ArgumentGuard.requireNotNull(edge, "edge");
		this.node = node;
		this.edge = edge;
	}

	/**
	 * Gets the node in the path that should contain the edge.
	 * 
	 * @return The node in the path that should contain the edge.
	 */
	public GraphNode getNode() {
		return this.node;
	}

	/**
	 * Gets the edge that is invalid with the node.
	 * 
	 * @return The edge that is invalid with the node.
	 */
	public GraphEdge getEdge() {
		return this.edge;
	}

	@Override
	public String getLocalizedMessage() {
		final var nodeIdentifier = this.node.getId().toString();
		final var nodeClassName = this.node.getClass().getSimpleName();
		final var edgeIdentifier = this.edge.getId().toString();
		final var edgeClassName = this.edge.getClass().getSimpleName();
		return MessageFormat.format(
				"Node \"{0}\" ({1}) and edge \"{2}\" ({3}) are invalid in the path.",
				nodeIdentifier,
				nodeClassName,
				edgeIdentifier,
				edgeClassName);
	}
}