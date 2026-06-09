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
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Represents a node in a Refactoring Advice Graph.
 */
public abstract class GraphNodeBase implements GraphNode {
	private final UUID id;
	
	/**
	 * The Refactoring Advice Graph to which this node belongs.
	 */
	protected final Graph graph;
	
	/**
	 * Initialises a new instance of a {@link GraphNodeBase}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeBase(Graph graph)
			throws ArgumentNullException {
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
	
	@Override
	public final UUID getId() {
		return this.id;
	}
	
	@Override
	public final Graph getGraph() {
		return this.graph;
	}
	
	@Override
	public final Set<GraphEdge> getEdges() {
		return this.graph.getEdgesFrom(this);
	}
	
	@Override
	public final <TEdge extends GraphEdge> Set<TEdge> getEdges(Class<TEdge> edgeType) {
		return this.graph.getEdgesFrom(this, edgeType);
	}
	
	@Override
	public final Set<GraphEdge> getEdgesIncoming() {
		return this.graph.getEdgesTo(this);
	}
	
	@Override
	public final <TEdge extends GraphEdge> Set<TEdge> getEdgesIncoming(Class<TEdge> edgeType) {
		return this.graph.getEdgesTo(this, edgeType);
	}
	
	@Override
	public final List<GraphPath> findPaths(int maximumDepth)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, maximumDepth, "maximumDepth");
		
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
	
	@Override
	public final List<GraphPath> findPaths(GraphNodeBase destinationNode, int maximumDepth)
			throws ArgumentNullException, IllegalArgumentException {
		ArgumentGuard.requireNotNull(destinationNode, "destinationNode");
		ArgumentGuard.requireGreaterThanOrEqual(0, maximumDepth, "maximumDepth");
		
		List<GraphPath> result = new ArrayList<>();
		Stack<GraphPathSearchNode> searchNodes = new Stack<>();
		var initialPath = new GraphPath(this);
		searchNodes.push(new GraphPathSearchNode(this, initialPath));
		
		while (!searchNodes.isEmpty()) {
			var currentSearchNode = searchNodes.pop();
			var currentNode = currentSearchNode.getNode();
			var currentPath = currentSearchNode.getPath();
			var currentPathSegments = currentPath.getSegments();
			
			if (currentNode.equals(destinationNode)) {
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
	
	@Override
	public abstract String getLabel();
	
	@Override
	public String getCaption() {
		return
				ResourceProvider
					.GraphNodeCaptions
					.getCaption(this.getClass());
	}
	
	/**
	 * Creates a clone of this node with identical attributes but belonging to the specified graph.
	 * @param graph The graph in which to insert the cloned node.
	 * @return The cloned node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public abstract GraphNodeBase clone(Graph graph) throws ArgumentNullException;
	
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
		if (!(other instanceof GraphNodeBase)) {
			return false;
		}
		return this.equals((GraphNodeBase)other);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (other == null) {
			return false;
		}
		return this.id.equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}