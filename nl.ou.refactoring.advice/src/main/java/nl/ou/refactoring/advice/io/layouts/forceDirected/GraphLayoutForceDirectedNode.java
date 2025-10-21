package nl.ou.refactoring.advice.io.layouts.forceDirected;

import java.awt.geom.Point2D;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutNode;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A node in a Force-Directed Layout, wraps a {@link GraphNode}.
 */
public final class GraphLayoutForceDirectedNode extends GraphLayoutNode {
	private Point2D.Double velocity;
	private Point2D.Double force;

	/**
	 * Initialises a new instance of {@link GraphLayoutForceDirectedNode}.
	 * @param node The actual Refactoring Advice Graph node.
	 * @throws ArgumentNullException Thrown if node is null.
	 */
	public GraphLayoutForceDirectedNode(GraphNode node)
			throws ArgumentNullException {
		super(node);
		this.velocity = new Point2D.Double(0, 0);
		this.force = new Point2D.Double(0, 0);
	}

	/**
	 * Gets the velocity of the node on the canvas.
	 * @return The velocity of the node on the canvas.
	 */
	public Point2D.Double getVelocity() {
		return this.velocity;
	}
	
	/**
	 * Gets the force of the node on the canvas.
	 * @return The force of the node on the canvas.
	 */
	public Point2D.Double getForce() {
		return this.force;
	}
}
