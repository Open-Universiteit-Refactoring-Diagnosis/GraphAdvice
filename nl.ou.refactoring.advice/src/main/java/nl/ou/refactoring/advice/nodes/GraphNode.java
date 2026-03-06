package nl.ou.refactoring.advice.nodes;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * An interface for graph node classes.
 */
public interface GraphNode {
	/**
	 * Gets the unique identifier of the node.
	 * @return The unique identifier of the node.
	 */
	public UUID getId();
	
	/**
	 * Gets the graph that contains the node.
	 * @return The graph that contains the node.
	 */
	public Graph getGraph();
	
	/**
	 * Gets the edges that depart from this node.
	 * @return An unmodifiable set of edges that depart from this node.
	 */
	public Set<GraphEdge> getEdges();
	
	/**
	 * Gets the edges that depart from this node, filtered by edgeType.
	 * @param <TEdge> The type of edge to filter.
	 * @param edgeType The type of edge to filter.
	 * @return An unmodifiable set of edges that depart from this node, filtered by edgeType.
	 */
	public <TEdge extends GraphEdge> Set<TEdge> getEdges(Class<TEdge> edgeType);
	
	/**
	 * Gets the edges that arrive to this node.
	 * @return An unmodifiable set of edges that arrive to this node.
	 */
	public Set<GraphEdge> getEdgesIncoming();
	
	/**
	 * Gets the edges that arrive to this node, filtered by edgeType.
	 * @param <TEdge> The type of edge to filter.
	 * @param edgeType The type of edge to filter.
	 * @return An unmodifiable set of edges that arrive to this node, filtered by edgeType.
	 */
	public <TEdge extends GraphEdge> Set<TEdge> getEdgesIncoming(Class<TEdge> edgeType);
	
	/**
	 * Finds paths that lead out of this node, limited by a maximum depth.
	 * @param maximumDepth The maximum depth of the paths.
	 * @return The paths that lead out of this node, limited by a maximum depth.
	 * @throws IllegalArgumentException Thrown if maximumDepth is not greater than or equal to 0.
	 */
	public List<GraphPath> findPaths(int maximumDepth)
		throws IllegalArgumentException;
	
	/**
	 * Finds all paths that lead to the specified destination node, restricted to a maximum depth.
	 * @param destinationNode The node to which to find paths from this node.
	 * @param maximumDepth The maximum depths of the paths to find.
	 * @return A list of paths found between this node and the destination node.
	 * @throws ArgumentNullException Thrown if destinationNode is null.
	 * @throws IllegalArgumentException Thrown if maximumDepth is not a positive integer.
	 */
	public List<GraphPath> findPaths(GraphNodeBase destinationNode, int maximumDepth)
		throws ArgumentNullException, IllegalArgumentException;
	
	/**
	 * Gets the label for the node.
	 * @return A label for the node.
	 */
	public String getLabel();
	
	/**
	 * Gets the caption for the node.
	 * @return A caption for the node.
	 */
	public String getCaption();
	
	/**
	 * Creates a clone of this node with identical attributes but belonging to the specified graph.
	 * @param graph The graph in which to insert the cloned node.
	 * @return The cloned node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNode clone(Graph graph)
		throws ArgumentNullException;
	
	/**
	 * Determines whether the current node and the other node are equal.
	 * @param other The other node to compare.
	 * @return True if the nodes are equal, otherwise false.
	 */
	public boolean equals(GraphNode other);
}