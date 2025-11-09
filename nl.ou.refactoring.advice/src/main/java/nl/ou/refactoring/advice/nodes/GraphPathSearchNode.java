package nl.ou.refactoring.advice.nodes;

import nl.ou.refactoring.advice.GraphPath;

/**
 * A node in a search for paths within a graph.
 */
public final class GraphPathSearchNode {
	private final GraphNode node;
	private final GraphPath path;

	/**
	 * Initialises a new instance of {@link GraphPathSearchNode}.
	 * @param node The last visited node.
	 * @param path The path for the last visited node.
	 */
	public GraphPathSearchNode(GraphNode node, GraphPath path) {
		this.node = node;
		this.path = path;
	}

	/**
	 * Gets the last visited node.
	 * @return The last visited node.
	 */
	public GraphNode getNode() {
		return this.node;
	}
	
	/**
	 * Gets the path for the last visited node.
	 * @return The path for the last visited node.
	 */
	public GraphPath getPath() {
		return this.path;
	}
}
