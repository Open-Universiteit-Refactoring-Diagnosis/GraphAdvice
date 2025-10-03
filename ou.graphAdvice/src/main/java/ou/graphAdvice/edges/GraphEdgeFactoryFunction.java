package ou.graphAdvice.edges;

import ou.graphAdvice.nodes.GraphNode;

/**
 * A functional interface that enables passing lambda functions for creating new Edges to a parameter in a method.
 * @param <TEdge> The type of Edge that is created.
 * @param <TNodeSource> The type of Node that is at the source of the Edge.
 * @param <TNodeDestination> The type of Node that is the destination of the Edge.
 */
@FunctionalInterface
public interface GraphEdgeFactoryFunction
	<TEdge extends GraphEdge,
	TNodeSource extends GraphNode,
	TNodeDestination extends GraphNode> {
	/**
	 * Creates a new edge.
	 * @param source The source node on the edge.
	 * @param destination The destination node on the edge.
	 * @param edgeLabels The labels of the edge.
	 * @return The newly created edge.
	 */
	TEdge create(TNodeSource source, TNodeDestination destination);
}
