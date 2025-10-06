package nl.ou.refactoring.advice;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * An exception that is thrown if multiple Edges of the same type are being connected to the same source Node and the same destination Node.
 */
public final class MultipleEdgesOfSameTypeException extends Exception {
	private final GraphEdge edge;
	
	/**
	 * Initialises a new instance of {@link MultipleEdgesOfSameTypeException}.
	 * @param sourceNode {@link GraphNode} The source Node that already has an Edge of the same type.
	 * @param destinationNode {@link GraphNode} The destination Node that already has an Edge of the same type.
	 * @param edge {@link GraphEdge} The Edge of the same type.
	 */
	public MultipleEdgesOfSameTypeException(GraphEdge edge) {
		this.edge = edge;
	}
	
	@Override
	public String getLocalizedMessage() {
		GraphNode sourceNode = this.edge.getSource();
		GraphNode destinationNode = this.edge.getDestination();
		return
				MessageFormat.format(
						"Multiple Edges of the same type '{0}' are not allowed. Source node identifier: {1}, destination node identifier: {2}",
						this.edge.getClass().getName(),
						sourceNode.getId().toString(),
						destinationNode.getId().toString());
	}

}
