package nl.ou.refactoring.advice.io.layouts;

import java.awt.geom.Point2D;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Represents a node in a particular graph layout.
 * Wraps an actual Refactoring Advice Graph {@link GraphNode}.
 */
public class GraphLayoutNode {
	private final GraphNode node;
	private Point2D.Double location;

	/**
	 * Initialises a new instance of {@link GraphLayoutNode}.
	 * @param node The actual Refactoring Advice Graph node to wrap with the layout node.
	 * @throws ArgumentNullException Thrown if node is null.
	 */
	public GraphLayoutNode(GraphNode node)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(node, "node");
		this.node = node;
		this.location = new Point2D.Double(0.0, 0.0);
	}

	/**
	 * Gets the actual Refactoring Advice Graph node that is wrapped by the layout node.
	 * @return The actual Refactoring Advice Graph node that is wrapped by the layout node.
	 */
	public GraphNode getNode() {
		return this.node;
	}
	
	/**
	 * Gets the current location of the node in the layout.
	 * @return The current location of the node in the layout.
	 */
	public Point2D.Double getLocation() {
		return this.location;
	}
}
