package nl.ou.refactoring.advice.nodes;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import nl.ou.refactoring.advice.Graph;
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