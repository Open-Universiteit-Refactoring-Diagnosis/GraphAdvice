package nl.ou.refactoring.advice.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * Represents a node in a Refactoring Advice Graph.
 */
public abstract class GraphNode {
	private final UUID id;
	protected final Graph graph;
	
	/**
	 * Initialises a new instance of a {@link GraphNode}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNode(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.id = UUID.randomUUID();
		this.graph = graph;
		this.ensureGraphContainsNode();
	}
	
	private void ensureGraphContainsNode() {
		if (!this.graph.containsNode(this)) {
			this.graph.addNode(this);
		}
	}
	
	/**
	 * Gets the unique identifier of the node.
	 * @return The unique identifier of the node.
	 */
	public UUID getId() {
		return this.id;
	}
	
	/**
	 * Gets the graph that contains the node.
	 * @return The graph that contains the node.
	 */
	public Graph getGraph() {
		return this.graph;
	}
	
	/**
	 * Gets the edges that depart from this node.
	 * @return The edges that depart from this node.
	 */
	public Set<GraphEdge> getEdges() {
		return this.graph.getEdgesFrom(this);
	}
	
	/**
	 * Finds paths that lead out of this node, limited by a maximum depth.
	 * @param maximumDepth The maximum depth of the paths.
	 * @return The paths that lead out of this node, limited by a maximum depth.
	 */
	public List<GraphPath> findPaths(int maximumDepth) {
		List<GraphPath> result = new ArrayList<>();
		Stack<GraphPathSearchNode> searchNodes = new Stack<>();
		var initialPath = new GraphPath(this);
		searchNodes.push(new GraphPathSearchNode(this, initialPath));
		
		while (!searchNodes.isEmpty()) {
			var currentSearchNode = searchNodes.pop();
			var currentNode = currentSearchNode.getNode();
			var currentPath = currentSearchNode.getPath();
			var currentPathSegments = currentPath.getSegments();
			
			if (currentPathSegments.size() <= maximumDepth) {
				result.add(currentPath);
			}
			if (currentPathSegments.size() >= maximumDepth) {
				continue;
			}
			
			var currentEdges = currentNode.getEdges();
			for (var edge : currentEdges) {
				var neighbour = edge.getDestinationNode();
				if (currentPath.contains(neighbour)) {
					// avoid cycles
					continue;
				}
				
				try {
					var newPath = (GraphPath)currentPath.clone();
					newPath.append(edge, neighbour);
					searchNodes.push(new GraphPathSearchNode(neighbour, newPath));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the label for the node.
	 * @return A label for the node.
	 */
	public abstract String getLabel();
	
	/**
	 * Gets the caption for the node.
	 * @return A caption for the node.
	 */
	public abstract String getCaption();
	
	/**
	 * Determines whether the current node and the other object are equal.
	 * <br />
	 * Overrides {@link Object#equals(Object)}.
	 * @param other The other object to compare.
	 * @return True if the objects are equal, otherwise false.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof GraphNode)) {
			return false;
		}
		return this.equals((GraphNode)other);
	}
	
	/**
	 * Determines whether the current node and the other node are equal.
	 * @param other The other node to compare.
	 * @return True if the nodes are equal, otherwise false.
	 */
	public boolean equals(GraphNode other) {
		if (other == null) {
			return false;
		}
		return this.id == other.id;
	}
}