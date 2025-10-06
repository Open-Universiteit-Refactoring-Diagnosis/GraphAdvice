package nl.ou.refactoring.advice.edges;

import nl.ou.refactoring.advice.nodes.GraphNode;

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
	 * @param sourceNode The source node on the edge.
	 * @param destinationNode The destination node on the edge.
	 * @param edgeLabels The labels of the edge.
	 * @return The newly created edge.
	 */
	TEdge create(TNodeSource sourceNode, TNodeDestination destinationNode);
}
