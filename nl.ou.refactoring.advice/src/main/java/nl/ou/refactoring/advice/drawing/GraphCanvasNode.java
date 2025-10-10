package nl.ou.refactoring.advice.drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeIntermediateRefactoring;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedy;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * A graph Node on a canvas; wraps a {@link GraphNode}.
 */
public final class GraphCanvasNode {
	private static final Font FONT_LABEL = new Font("Helvetica", Font.BOLD, 12);
	private static final Font FONT_CAPTION = new Font("Helvetica", Font.PLAIN, 10);
	private final GraphNode node;
	private Point2D.Double position;
	private Point2D.Double velocity;
	private Point2D.Double force;
	
	/**
	 * Initialises a new instance of {@link GraphCanvasNode}.
	 * @param node The node that is represented.
	 */
	public GraphCanvasNode(GraphNode node) {
		this.node = node;
		this.position = new Point2D.Double(0, 0);
		this.velocity = new Point2D.Double(0, 0);
		this.force = new Point2D.Double(0, 0);
	}

	/**
	 * Gets the node that is represented.
	 * @return The node that is represented.
	 */
	public GraphNode getNode() {
		return this.node;
	}
	
	/**
	 * Gets the position of the node on the canvas.
	 * @return The position of the node on the canvas.
	 */
	public Point2D.Double getPosition() {
		return this.position;
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
	
	/**
	 * Draws the node on a graphics surface.
	 * @param graphics The graphics surface to draw the node on.
	 */
	public void draw(Graphics2D graphics) {
		// Measurements
		final var labelFontMetrics = graphics.getFontMetrics(FONT_LABEL);
		final var label = this.node.getLabel();
		final var labelWidth = labelFontMetrics.stringWidth(label);
		final var labelHeight = labelFontMetrics.getHeight();
		final var captionFontMetrics = graphics.getFontMetrics(FONT_CAPTION);
		final var caption = this.node.getCaption();
		final var captionWidth = captionFontMetrics.stringWidth(caption);
		final var captionHeight = captionFontMetrics.getHeight();
		final var ovalWidth = Math.max(100, Math.max(labelWidth + 20, captionWidth + 20));
		final var ovalHeight = labelHeight + captionHeight + 20;
		
		// Node oval
		graphics.setColor(this.getFillColour());
		final var x = this.position.x - ovalWidth / 2;
		final var y = this.position.y - ovalHeight / 2;
		graphics.fillOval((int)x, (int)y, ovalWidth, ovalHeight);
		
		// Node label
		final var labelX = x + (ovalWidth - labelWidth) / 2;
		final var labelY = y + labelHeight + 5;
		graphics.setColor(this.getColour());
		graphics.setFont(FONT_LABEL);
		graphics.drawString(label, (int)labelX, (int)labelY);
		
		// Node caption
		final var captionX = x + (ovalWidth - captionWidth) / 2;
		final var captionY = y + labelHeight + captionHeight + 5;
		graphics.setFont(FONT_CAPTION);
		graphics.drawString(caption, (int)captionX, (int)captionY);
	}
	
	private Color getColour() {
		return switch(this.node) {
			case GraphNodeCode _ -> Color.white;
			case GraphNodeMicrostep _ -> Color.black;
			case GraphNodeRemedy _ -> Color.black;
			case GraphNodeRisk _ -> Color.black;
			default -> Color.black;
		};
	}
	
	private Color getFillColour() {
		return switch(this.node) {
			case GraphNodeAttribute _ -> Color.decode("#60A917");
			case GraphNodeClass _ -> Color.decode("#0050EF");
			case GraphNodeIntermediateRefactoring _ -> Color.decode("#F5F5F5");
			case GraphNodeMicrostep _ -> Color.decode("#DAE8FC");
			case GraphNodeOperation _ -> Color.decode("#6A00FF");
			case GraphNodePackage _ -> Color.decode("#D80073");
			case GraphNodeRefactoringStart _ -> Color.decode("#F5F5F5");
			case GraphNodeRemedy _ -> Color.decode("#D5E8D4");
			case GraphNodeRisk _ -> Color.decode("#FFE6CC");
			default -> Color.white;
		};
	}
}
