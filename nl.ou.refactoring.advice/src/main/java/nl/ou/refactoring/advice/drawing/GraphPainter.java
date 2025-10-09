package nl.ou.refactoring.advice.drawing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Paints a Refactoring Advice Graph on graphics surfaces.
 */
public final class GraphPainter {
	// Force-directed layout
	private static final int ITERATIONS = 500000;
	private static final double REPULSION_CONSTANT = 1000;
	private static final double SPRING_LENGTH = 200;
	private static final double SPRING_STRENGTH = 0.1;
	private static final double DAMPING = 0.85;
	private static final double TIME_STEP = 0.5;
	
	// Drawing
	private static final Font FONT_EDGE = new Font("Helvetica", Font.PLAIN, 10);
	private final double width;
	private final double height;
	
	/**
	 * Initialises a new instance of {@link GraphCanvas}.
	 * @param width The desired width of the graph drawing.
	 * @param height The desired height of the graph drawing.
	 */
	public GraphPainter(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Draws a Refactoring Advice Graph on the canvas.
	 * @param graph The Refactoring Advice Graph to draw on the canvas.
	 */
	public void draw(Graph graph, Graphics2D graphics) {
		// Create a map of drawable nodes (wrappers for real nodes).
		final var nodesMap =
				graph
					.getNodes()
					.stream()
					.collect(Collectors.toMap(node -> node, node -> new GraphCanvasNode(node)));
		final var nodes =
				nodesMap
					.values()
					.stream()
					.collect(Collectors.toSet());
		final var edges = graph.getEdges();
		
		// Initialise nodes in centre.
		final var centreX = this.width / 2;
		final var centreY = this.height / 2;
		for (var node : nodes) {
			node
				.getPosition()
				.setLocation(
						centreX + new Random().nextDouble(10),
						centreY + new Random().nextDouble(10));
		}
		for (var i = 0; i < ITERATIONS; i++) {
			applyRepulsion(nodes);
			applyAttraction(nodesMap, edges);
			updatePositions(nodes);
		}
		for (var edge : edges) {
			final var sourceNode = edge.getSourceNode();
			final var sourceNodeDrawable = nodesMap.get(sourceNode);
			final var destinationNode = edge.getDestinationNode();
			final var destinationNodeDrawable = nodesMap.get(destinationNode);
			drawEdge(graphics, edge, sourceNodeDrawable, destinationNodeDrawable);
		}
		for (var node : nodes) {
			node.draw(graphics);
		}
	}
	
	/**
	 * Creates an image from a Refactoring Advice Graph.
	 * @param graph The graph from which to create an image.
	 * @return An image of the contents of the canvas.
	 */
	public Image createImage(Graph graph) {
		final var image = new BufferedImage((int)this.width, (int)this.height, BufferedImage.TYPE_INT_ARGB);
		final var graphics = image.createGraphics();
		this.draw(graph, graphics);
		return image;
	}
	
	private static void drawEdge(
			Graphics2D graphics,
			GraphEdge edge,
			GraphCanvasNode sourceNode,
			GraphCanvasNode destinationNode) {
		final var sourceNodePosition = sourceNode.getPosition();
		final var destinationNodePosition = destinationNode.getPosition();
		
		// Line
		graphics.setColor(Color.cyan);
		graphics.drawLine(
				(int)sourceNodePosition.x,
				(int)sourceNodePosition.y,
				(int)destinationNodePosition.x,
				(int)destinationNodePosition.y);
		
		// Label
		final var labelX = (destinationNodePosition.x + sourceNodePosition.x) / 2;
		final var labelY = (destinationNodePosition.y + sourceNodePosition.y) / 2;
		final var fontMetrics = graphics.getFontMetrics(FONT_EDGE);
		final var label = edge.getLabel();
		final var labelWidth = fontMetrics.stringWidth(label);
		final var labelHeight = fontMetrics.getHeight();
		graphics.setFont(FONT_EDGE);
		graphics.drawString(label, (int)labelX - (labelWidth / 2), (int)labelY + labelHeight / 2);
		
		// Arrow
		final var arrowRadius = Math.max(labelWidth + 10, labelHeight + 10);
		final var arrowAngle =
				Math.atan2(
						destinationNodePosition.y - sourceNodePosition.y,
						destinationNodePosition.x - sourceNodePosition.x);
		final var arrowWidth = 15;
		final var arrowHeight = 7;
		
		final var arrowTipX = labelX + Math.cos(arrowAngle) * arrowRadius;
		final var arrowTipY = labelY + Math.sin(arrowAngle) * arrowRadius;
		final var arrowPerpendicularX = -Math.sin(arrowAngle);
		final var arrowPerpendicularY = Math.cos(arrowAngle);
		final var arrowBaseCentreX = arrowTipX - Math.cos(arrowAngle) * arrowHeight;
		final var arrowBaseCentreY = arrowTipY - Math.sin(arrowAngle) * arrowHeight;
		final var arrowBaseLeftX = arrowBaseCentreX + arrowPerpendicularX * (arrowWidth / 2);
		final var arrowBaseLeftY = arrowBaseCentreY + arrowPerpendicularY * (arrowWidth / 2);
		final var arrowBaseRightX = arrowBaseCentreX + arrowPerpendicularX * (arrowWidth / 2);
		final var arrowBaseRightY = arrowBaseCentreY + arrowPerpendicularY * (arrowWidth / 2);
		
		final var triangleXs =
				new int[] {
						(int)arrowTipX,
						(int)arrowBaseRightX,
						(int)arrowBaseLeftX
				};
		final var triangleYs =
				new int[] {
						(int)arrowTipY,
						(int)arrowBaseRightY,
						(int)arrowBaseLeftY
				};
		final var arrow = new Polygon(triangleXs, triangleYs, 3);
		graphics.fillPolygon(arrow);
	}
	
	private static void applyRepulsion(Set<GraphCanvasNode> nodes) {
		for (var node : nodes) {
			node.getForce().setLocation(0, 0);
			for (var nodeReference : nodes) {
				if (node == nodeReference) {
					continue;
				}
				final var differenceX = node.getPosition().x - nodeReference.getPosition().x;
				final var differenceY = node.getPosition().y - nodeReference.getPosition().y;
				final var distance = Math.max(1,  Math.hypot(differenceX, differenceY));
				final var repulsion = REPULSION_CONSTANT / (distance * distance);
				node.getForce().x += (differenceX / distance) * repulsion;
				node.getForce().y += (differenceY / distance) * repulsion;
			}
		}
	}
	
	private static void applyAttraction(Map<GraphNode, GraphCanvasNode> nodes, Set<GraphEdge> edges) {
		for (var edge : edges) {
			final var graphicsNodeSource = nodes.get(edge.getSourceNode());
			final var graphicsNodeDestination = nodes.get(edge.getDestinationNode());
			final var differenceX = graphicsNodeDestination.getPosition().x - graphicsNodeSource.getPosition().x;
			final var differenceY = graphicsNodeDestination.getPosition().y - graphicsNodeSource.getPosition().y;
			final var distance = Math.max(1,  Math.hypot(differenceX, differenceY));
			final var displacement = distance - SPRING_LENGTH;
			final var force = SPRING_STRENGTH * displacement;
			final var forceX = (differenceX / distance) * force;
			final var forceY = (differenceY / distance) * force;
			
			graphicsNodeSource.getForce().x += forceX;
			graphicsNodeSource.getForce().y += forceY;
			graphicsNodeDestination.getForce().x -= forceX;
			graphicsNodeDestination.getForce().y -= forceY;
		}
	}
	
	private static void updatePositions(Set<GraphCanvasNode> nodes) {
		for (var node : nodes) {
			node.getVelocity().x = (node.getVelocity().x + node.getForce().x * TIME_STEP) * DAMPING;
			node.getVelocity().y = (node.getVelocity().y + node.getForce().y * TIME_STEP) * DAMPING;
			node.getPosition().x += node.getVelocity().x * TIME_STEP;
			node.getPosition().y += node.getVelocity().y * TIME_STEP;
		}
	}
}
