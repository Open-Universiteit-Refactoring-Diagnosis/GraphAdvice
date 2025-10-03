package ou.graphAdvice;

import java.util.Collections;
import java.util.HashSet;
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
	private final Set<GraphNode> nodes;
	private final Set<GraphEdge> edges;
	
	/**
	 * Initialises a new instance of {@link Graph}.
	 */
	public Graph() {
		this.id = UUID.randomUUID();
		this.nodes = new HashSet<GraphNode>();
		this.edges = new HashSet<GraphEdge>();
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
		return Collections.unmodifiableSet(this.nodes);
	}
	
	/**
	 * Gets the nodes in this graph of a particular type.
	 * @param <TNode> The type of node to retrieve.
	 * @param nodeType The type of node to retrieve.
	 * @return A read-only set of nodes of the requested type in this graph.
	 * @apiNote The returned {@link Set Set&lt;TNode&gt;} is not modifiable.
	 */
	public <TNode extends GraphNode> Set<TNode> getNodes(Class<TNode> nodeType) {
		return Collections.unmodifiableSet(
				this.nodes.stream()
					.filter(nodeType::isInstance)
					.map(nodeType::cast)
					.collect(Collectors.toSet())
				);
	}
	
	/**
	 * Adds a node to the Refactoring Advice Graph.
	 * @param node The node to add to the Refactoring Advice Graph.
	 * @throws ArgumentNullException Thrown if node is null.
	 */
	public void addNode(GraphNode node) {
		ArgumentGuard.requireNotNull(node, "node");
		this.nodes.add(node);
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
		return this.nodes.contains(node);
	}
	
	/**
	 * Gets the edges in the Refactoring Advice Graph.
	 * @return The edges in the Refactoring Advice Graph.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdges() {
		return Collections.unmodifiableSet(this.edges);
	}
	
	/**
	 * Gets the edges of which its source is the specified node.
	 * @param sourceNode The source node.
	 * @return The edges of which its source is the specified node.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdgesFrom(GraphNode sourceNode) {
		if (sourceNode == null) {
			return Set.of();
		}
		return Collections.unmodifiableSet(
				this
					.edges
					.stream()
					.filter(edge -> edge.getSource().equals(sourceNode))
					.collect(Collectors.toSet()));
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
			return Set.of();
		}
		return Collections.unmodifiableSet(
				this
					.edges
					.stream()
					.filter(edge -> edge.getSource().equals(sourceNode) && edge.getDestination().equals(destinationNode))
					.collect(Collectors.toSet()));
	}
	
	/**
	 * Gets the edges of which its destination is the specified node.
	 * @param destinationNode The destination node.
	 * @return The edges of which its destination is the specified node.
	 * @apiNote The returned {@link Set Set&lt;GraphEdge&gt;} is not modifiable.
	 */
	public Set<GraphEdge> getEdgesTo(GraphNode destinationNode) {
		if (destinationNode == null) {
			return Set.of();
		}
		return Collections.unmodifiableSet(
				this
					.edges
					.stream()
					.filter(edge -> edge.getDestination().equals(destinationNode))
					.collect(Collectors.toSet()));
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
		
		Set<TEdge> edges =
				this
					.getEdgesWith(sourceNode, destinationNode)
					.stream()
					.filter(edgeClass::isInstance)
					.map(edgeClass::cast)
					.collect(Collectors.toSet());
		if (edges.size() > 0) {
			return (TEdge)edges.stream().findFirst().get();
		}
		
		TEdge edge = edgeFactory.create(sourceNode, destinationNode);
		this.edges.add(edge);
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
		return this
				.nodes
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
