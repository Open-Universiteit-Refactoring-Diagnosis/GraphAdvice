package ou.graphAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import ou.graphAdvice.contracts.ArgumentGuard;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.edges.GraphEdgeFactoryFunction;
import ou.graphAdvice.nodes.GraphNode;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoringStart;
import ou.graphAdvice.nodes.refactoring.RefactoringMayContainOnlyOneStartNodeException;

/**
 * Represents a Refactoring Advice Graph.
 */
public final class Graph {
	private final UUID id;
	private final Map<GraphNode, Map<GraphNode, Set<GraphEdge>>> matrix;
	
	/**
	 * Initialises a new instance of {@link Graph}.
	 */
	public Graph() {
		this.id = UUID.randomUUID();
		this.matrix = new HashMap<>();
	}
	
	/**
	 * Gets the unique identifier of the Refactoring Advice Graph.
	 * @return The unique identifier of the Refactoring Advice Graph.
	 */
	public UUID getId() {
		return this.id;
	}
	
	/**
	 * Gets the nodes in the Refactoring Advice Graph.
	 * @return The nodes in the Refactoring Advice Graph.
	 * @apiNote The returned {@link Set Set&lt;GraphNode&gt;} is not modifiable.
	 */
	public Set<GraphNode> getNodes() {
		return Collections.unmodifiableSet(this.matrix.keySet());
	}
	
	/**
	 * Gets the nodes in this graph that are assignable to a particular type.
	 * @param <TNode> The type of node to retrieve.
	 * @param nodeType The type of node to retrieve.
	 * @return A read-only set of nodes that are assignable to the requested type in this graph.
	 * @throws ArgumentNullException Thrown if nodeType is null.
	 * @apiNote The returned {@link Set Set&lt;TNode&gt;} is not modifiable.
	 */
	public <TNode extends GraphNode> Set<TNode> getNodes(Class<TNode> nodeType)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(nodeType, "nodeType");
		return
				Collections.unmodifiableSet(
					this
						.getNodes()
						.stream()
						.filter(nodeType::isInstance)
						.map(nodeType::cast)
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Gets the nodes in this graph that are exactly of a particular type.
	 * @param <TNode> The type of node to retrieve.
	 * @param nodeType The type of node to retrieve.
	 * @return A read-only set of nodes of the requested type in this graph.
	 * @throws ArgumentNullException Thrown if nodeType is null.
	 * @apiNote The returned {@link Set Set&lt;TNode&gt;} is not modifiable.
	 */
	public <TNode extends GraphNode> Set<TNode> getNodesExact(Class<TNode> nodeType) {
		ArgumentGuard.requireNotNull(nodeType, "nodeType");
		return
				Collections.unmodifiableSet(
					this
						.getNodes()
						.stream()
						.filter(node -> node.getClass() == nodeType)
						.map(nodeType::cast)
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Adds a node to the Refactoring Advice Graph. If the node is already present, nothing happens.
	 * @param node The node to add to the Refactoring Advice Graph.
	 * @throws ArgumentNullException Thrown if node is null.
	 */
	public void addNode(GraphNode node) {
		ArgumentGuard.requireNotNull(node, "node");
		this.matrix.putIfAbsent(node, new HashMap<>());
	}
	
	/**
	 * Determines whether the graph contains a node.
	 * @param node The node to check whether the graph contains it.
	 * @return True if the node is already in the graph, false if not.
	 */
	public boolean containsNode(GraphNode node) {
		if (node == null) {
			return false;
		}
		return this.matrix.containsKey(node);
	}
	
	/**
	 * Gets the edges in the Refactoring Advice Graph.
	 * @return The edges in the Refactoring Advice Graph.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdges() {
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.values()
						.stream()
						.flatMap(row -> row.values().stream())
						.reduce(new HashSet<>(), (aggregate, next) -> { aggregate.addAll(next); return aggregate; })
				);
	}
	
	/**
	 * Gets the edges of which its source is the specified node.
	 * @param sourceNode The source node.
	 * @return The edges of which its source is the specified node.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdgesFrom(GraphNode sourceNode) {
		if (sourceNode == null) {
			return Collections.unmodifiableSet(Set.of());
		}
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.getOrDefault(sourceNode, new HashMap<>())
						.values()
						.stream()
						.flatMap(columns -> columns.stream())
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Gets the edges of which its source is the specified node and the edge is the specified type.
	 * @param <TEdge> The requested type of edge.
	 * @param sourceNode The source node.
	 * @param edgeType The requested type of edge.
	 * @return The edges of which its source is the specified node and the edge is of the specified type.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public <TEdge extends GraphEdge> Set<TEdge> getEdgesFrom(GraphNode sourceNode, Class<TEdge> edgeType) {
		if (sourceNode == null) {
			return Collections.unmodifiableSet(Set.of());
		}
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.getOrDefault(sourceNode, new HashMap<>())
						.values()
						.stream()
						.flatMap(
								columns ->
								columns
									.stream()
									.filter(edgeType::isInstance)
									.map(edgeType::cast))
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Gets the edges that are connected to a specified source node and a specified destination node.
	 * @param sourceNode The source node.
	 * @param destinationNode The destination node.
	 * @return The edges that are connected from the source node to the destination node.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdgesWith(GraphNode sourceNode, GraphNode destinationNode) {
		if (sourceNode == null || destinationNode == null) {
			return Collections.unmodifiableSet(Set.of());
		}
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.getOrDefault(sourceNode, new HashMap<>())
						.getOrDefault(destinationNode, new HashSet<>())
				);
	}
	
	/**
	 * Gets the edges of which its destination is the specified node.
	 * @param destinationNode The destination node.
	 * @return The edges of which its destination is the specified node.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdgesTo(GraphNode destinationNode) {
		if (destinationNode == null) {
			return Collections.unmodifiableSet(Set.of());
		}
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.values()
						.stream()
						.flatMap(rows -> rows.getOrDefault(destinationNode, new HashSet<>()).stream())
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Adds an edge to the graph, if it does not already exist.
	 * If it already exists, it returns the existing instance.
	 * @param <TEdge> The type of edge.
	 * @param <TNodeSource> The type of source node.
	 * @param <TNodeDestination> The type of destination node.
	 * @param sourceNode The source node of the edge.
	 * @param destinationNode The destination node of the edge.
	 * @param edgeFactory The factory method for creating a new instance of the desired edge, if it does not already exist.
	 * @return If the edge is new, the newly created edge. If the edge already exists, the existing edge.
	 * @throws ArgumentNullException Thrown if sourceNode, destinationNode, edgeFactory or edgeClass is null.
	 */
	public <TEdge extends GraphEdge, TNodeSource extends GraphNode, TNodeDestination extends GraphNode>
		TEdge addEdge(
				TNodeSource sourceNode,
				TNodeDestination destinationNode,
				GraphEdgeFactoryFunction<TEdge, TNodeSource, TNodeDestination> edgeFactory,
				Class<TEdge> edgeClass)
						throws ArgumentNullException {
		ArgumentGuard.requireNotNull(sourceNode, "sourceNode");
		ArgumentGuard.requireNotNull(destinationNode, "destinationNode");
		ArgumentGuard.requireNotNull(edgeFactory, "edgeFactory");
		ArgumentGuard.requireNotNull(edgeClass, "edgeClass");
		
		Set<GraphEdge> edges =
				this
					.matrix
					.computeIfAbsent(sourceNode, _ -> new HashMap<>())
					.computeIfAbsent(destinationNode, _ -> new HashSet<>());
		TEdge edge =
				edges
					.stream()
					.filter(knownEdge -> knownEdge.getClass() == edgeClass)
					.map(edgeClass::cast)
					.findFirst()
					.orElse(null);
		if (edge == null) {
			edge = edgeFactory.create(sourceNode, destinationNode);
			edges.add(edge);
		}
		return edge;
	}
	
	/**
	 * Starts a new refactoring.
	 * @return The start node of the refactoring.
	 * @throws RefactoringMayContainOnlyOneStartNodeException Thrown if the refactoring has already started.
	 */
	public GraphNodeRefactoringStart start()
			throws RefactoringMayContainOnlyOneStartNodeException {
		return new GraphNodeRefactoringStart(this);
	}
	
	/**
	 * Gets the start node of the refactoring.
	 * @return The start node of the refactoring, if present, otherwise null.
	 */
	public GraphNodeRefactoringStart getStart() {
		return
				this
					.matrix
					.keySet()
					.stream()
					.filter(GraphNodeRefactoringStart.class::isInstance)
					.map(GraphNodeRefactoringStart.class::cast)
					.findFirst()
					.orElse(null);
	}
	
	/**
	 * Determines whether Refactoring Advice Graphs are equal.
	 * <br />
	 * This method overrides {@link Object#equals(Object)}.
	 * @param other The other Refactoring Advice Graph.
	 * @return True if equal, false if not.
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Graph)) {
			return false;
		}
		return this.equals((Graph)other);
	}
	
	/**
	 * Determines whether Refactoring Advice Graphs are equal.
	 * @param other The other Refactoring Advice Graph.
	 * @return True if equal, false if not.
	 */
	public boolean equals(Graph other) {
		if (other == null) {
			return false;
		}
		return this.id == other.id;
	}
}
