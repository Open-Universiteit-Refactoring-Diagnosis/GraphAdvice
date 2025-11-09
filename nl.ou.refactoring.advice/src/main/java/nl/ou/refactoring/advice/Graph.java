package nl.ou.refactoring.advice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.edges.GraphEdgeFactoryFunction;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;

/**
 * Represents a Refactoring Advice Graph.
 */
public final class Graph {
	private final UUID id;
	private final Map<GraphNode, Map<GraphNode, Set<GraphEdge>>> matrix;
	private final String refactoringName;
	
	/**
	 * Initialises a new instance of {@link Graph}.
	 * @param refactoringName The name of the refactoring.
	 * @throws ArgumentNullException Thrown if refactoringName is null.
	 * @throws ArgumentEmptyException Thrown if refactoringName is empty or contains only white spaces.
	 */
	public Graph(String refactoringName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(refactoringName, "refactoringName");
		this.id = UUID.randomUUID();
		this.matrix = new HashMap<>();
		this.refactoringName = refactoringName;
	}
	
	/**
	 * Gets the unique identifier of the Refactoring Advice Graph.
	 * @return The unique identifier of the Refactoring Advice Graph.
	 */
	public UUID getId() {
		return this.id;
	}
	
	/**
	 * Gets a {@link GraphNode} by its unique identifier.
	 * @param nodeIdentifier The unique identifier of the node.
	 * @return The node with the specified identifier, or if not found, null.
	 * @throws ArgumentNullException Thrown if nodeIdentifier is null.
	 */
	public GraphNode getNode(UUID nodeIdentifier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(nodeIdentifier, "identifier");
		return
				this
					.matrix
					.keySet()
					.stream()
					.filter(node -> node.getId() == nodeIdentifier)
					.findFirst()
					.orElse(null);
	}
	
	/**
	 * Gets the nodes in the Refactoring Advice Graph.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphNode&gt;} is not modifiable.
	 * @return The nodes in the Refactoring Advice Graph.
	 */
	public Set<GraphNode> getNodes() {
		return Collections.unmodifiableSet(this.matrix.keySet());
	}
	
	/**
	 * Gets the nodes in this graph that are assignable to a particular type.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;TNode&gt;} is not modifiable.
	 * @param <TNode> The type of node to retrieve.
	 * @param nodeType The type of node to retrieve.
	 * @return A read-only set of nodes that are assignable to the requested type in this graph.
	 * @throws ArgumentNullException Thrown if nodeType is null.
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
	 * Gets the nodes in this graph that are exactly of a particular type.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;TNode&gt;} is not modifiable.
	 * @param <TNode> The type of node to retrieve.
	 * @param nodeType The type of node to retrieve.
	 * @return A read-only set of nodes of the requested type in this graph.
	 * @throws ArgumentNullException Thrown if nodeType is null.
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
	 * Removes nodes of type nodeType and associated edges (both incoming and outgoing) from the graph.
	 * @param <TNode> The type of node to remove.
	 * @param nodeType The type of node to remove.
	 * @throws ArgumentNullException Thrown if nodeType is null.
	 */
	public <TNode extends GraphNode> void removeNodes(Class<TNode> nodeType)
			throws ArgumentNullException {
		final var nodes = this.getNodesExact(nodeType);
		for (var node : nodes) {
			this.matrix.forEach((_, column) -> {
				column.remove(node);
			});
			this.matrix.remove(node);
		}
	}
	
	/**
	 * Gets a {@link GraphEdge} by its unique identifier.
	 * @param edgeIdentifier The unique identifier of the edge.
	 * @return The edge with the specified identifier, or if not found, null.
	 * @throws ArgumentNullException Thrown if edgeIdentifier is null.
	 */
	public GraphEdge getEdge(UUID edgeIdentifier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(edgeIdentifier, "edgeIdentifier");
		return
				this
					.matrix
					.values()
					.stream()
					.flatMap(column -> column.values().stream())
					.flatMap(edges -> edges.stream())
					.filter(edge -> edge.getId() == edgeIdentifier)
					.findFirst()
					.orElse(null);
	}
	
	/**
	 * Gets the edges in the Refactoring Advice Graph.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @return The edges in the Refactoring Advice Graph.
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
	 * Gets the edges of which its source is the specified node.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @param sourceNode The source node.
	 * @return The edges of which its source is the specified node.
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
	 * Gets the edges of which its source is the specified node and the edge is the specified type.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @param <TEdge> The requested type of edge.
	 * @param sourceNode The source node.
	 * @param edgeType The requested type of edge.
	 * @return The edges of which its source is the specified node and the edge is of the specified type.
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
	 * Gets the edges that are connected to a specified source node and a specified destination node.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @param sourceNode The source node.
	 * @param destinationNode The destination node.
	 * @return The edges that are connected from the source node to the destination node.
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
	 * Gets the edges of which its destination is the specified node.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @param destinationNode The destination node.
	 * @return The edges of which its destination is the specified node.
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
						.flatMap(
								columns ->
								columns.getOrDefault(destinationNode, new HashSet<>()).stream())
						.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Gets the edges of which its source is the specified node and the edge is the specified type.<br />
	 * <strong>Note:</strong> The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 * @param <TEdge> The requested type of edge.
	 * @param destinationNode The destination node.
	 * @param edgeType The requested type of edge.
	 * @return The edges of which its source is the specified node and the edge is of the specified type.
	 */
	public <TEdge extends GraphEdge> Set<TEdge> getEdgesTo(GraphNode destinationNode, Class<TEdge> edgeType) {
		if (destinationNode == null) {
			return Collections.unmodifiableSet(Set.of());
		}
		return
				Collections.unmodifiableSet(
					this
						.matrix
						.values()
						.stream()
						.flatMap(
								columns ->
								columns
									.getOrDefault(destinationNode, new HashSet<>())
									.stream()
									.filter(edgeType::isInstance)
									.map(edgeType::cast))
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
	 * @param edgeClass The type of edge.
	 * @return If the edge is new, the newly created edge. If the edge already exists, the existing edge.
	 * @throws ArgumentNullException Thrown if sourceNode, destinationNode, edgeFactory or edgeClass is null.
	 */
	public <TEdge extends GraphEdge, TNodeSource extends GraphNode, TNodeDestination extends GraphNode>
		TEdge getOrAddEdge(
				TNodeSource sourceNode,
				TNodeDestination destinationNode,
				GraphEdgeFactoryFunction<TEdge, TNodeSource, TNodeDestination> edgeFactory,
				Class<TEdge> edgeClass)
						throws ArgumentNullException {
		ArgumentGuard.requireNotNull(sourceNode, "sourceNode");
		ArgumentGuard.requireNotNull(destinationNode, "destinationNode");
		ArgumentGuard.requireNotNull(edgeFactory, "edgeFactory");
		ArgumentGuard.requireNotNull(edgeClass, "edgeClass");
		
		final var edges =
				this
					.matrix
					.computeIfAbsent(sourceNode, _ -> new HashMap<>())
					.computeIfAbsent(destinationNode, _ -> new HashSet<>());
		var edge =
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
		return new GraphNodeRefactoringStart(this, this.refactoringName);
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
	 * Gets the name of the refactoring.
	 * @return The name of the refactoring.
	 */
	public String getRefactoringName() {
		return this.refactoringName;
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
