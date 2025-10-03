package ou.graphAdvice.edges;

import ou.graphAdvice.nodes.GraphNode;

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
