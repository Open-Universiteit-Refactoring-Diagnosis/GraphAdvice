package nl.ou.refactoring.advice.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
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
	 * Creates a deep clone of this node with (also) cloned dependent nodes and edges, inserting them in the specified graph.
	 * @param graph The graph in which to insert the cloned node.
	 * @param executor The executor responsible for running clone tasks.
	 * @param tasks The tasks for cloning each node so dependent nodes can wait until other nodes are cloned.
	 * @return The cloned node.
	 * @throws ArgumentNullException Thrown if graph, executor or tasks is null.
	 */
	public Future<GraphNodeBase> deepClone(
		Graph graph,
		Executor executor,
		Map<GraphNodeBase, Future<GraphNodeBase>> tasks
	) throws ArgumentNullException {
		// Already cloned, return directly.
		if (tasks.containsKey(this)) {
			return tasks.get(this);
		}
		
		return tasks.computeIfAbsent(
			this,
			(_) -> {
				final var result = new CompletableFuture<GraphNodeBase>();
				executor.execute(() -> {
					try {
						// Shallow clone first.
						final var thisCloned = this.clone(graph);
						result.complete(thisCloned);
						
						// Clone nodes from outgoing edges.
						final var outgoingEdges = this
								.getEdges()
								.stream()
								.filter((e) -> !GraphNodeIdentifier.class.isInstance(e.getDestinationNode()))
								.collect(Collectors.toUnmodifiableSet());
						final var outgoingEdgeFutures = new ArrayList<CompletableFuture<Void>>();
						for (final var outgoingEdge : outgoingEdges) {
							final var destinationNode = outgoingEdge.getDestinationNode();
							if (destinationNode instanceof GraphNodeBase) {
								final var destinationNodeBase = (GraphNodeBase)destinationNode;
								final var destinationNodeFuture =
									(CompletableFuture<GraphNodeBase>)tasks.computeIfAbsent(
										destinationNodeBase,
										(node) -> node.deepClone(graph, executor, tasks)
									);
								
								outgoingEdgeFutures.add(
									destinationNodeFuture.thenAccept((destinationNodeCloned) -> {
										graph.addEdge(outgoingEdge.clone(thisCloned, destinationNodeCloned));
									})
								);
							}
						}
					
						// Return the clone.
						CompletableFuture
							.allOf(outgoingEdgeFutures.toArray(new CompletableFuture[0]))
							.thenRun(() -> result.complete(thisCloned))
							.exceptionally((ex) -> {
								result.completeExceptionally(ex);
								return null;
							});
					}
					catch (Exception ex) {
						result.completeExceptionally(ex);
					}
				});
				return result;
			}
		);
	}
	
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
}