package nl.ou.refactoring.advice.io.layouts.sugiyama;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A temporary edge in a Sugiyama (Layered) Layout graph.
 */
public final class GraphLayoutSugiyamaEdge extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphLayoutSugiyamaEdge}.
	 * @param sourceNode The source node that is the origin of the edge.
	 * @param destinationNode The destination node that is the target of the edge.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphLayoutSugiyamaEdge(
			GraphNode sourceNode,
			GraphNode destinationNode)
					throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}

	@Override
	public String getLabel() {
		return "Temporary";
	}
}
