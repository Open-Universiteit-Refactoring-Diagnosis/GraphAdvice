package nl.ou.refactoring.advice.io.images;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutNode;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutSettings;
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
 * Paints a Refactoring Advice Graph on graphics surfaces.
 */
public final class GraphPainter {
	// Drawing
	private static final String FONT_NAME = "Helvetica";
	private static final Font FONT_EDGE = new Font(FONT_NAME, Font.PLAIN, 10);
	private static final Font FONT_LABEL = new Font(FONT_NAME, Font.BOLD, 12);
	private static final Font FONT_CAPTION = new Font(FONT_NAME, Font.PLAIN, 10);
	private final double width;
	private final double height;
	
	/**
	 * Initialises a new instance of {@link GraphPainter}.
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
	 * @param graphics The graphics surface to draw on.
	 * @param layoutSettings The graph layout settings.
	 * @throws ArgumentNullException Thrown if graph, graphics or layoutSettings is null.
	 */
	public void draw(Graph graph, Graphics2D graphics, GraphLayoutSettings layoutSettings)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(graphics, "graphics");
		ArgumentGuard.requireNotNull(layoutSettings, "layoutSettings");
		final var layout = layoutSettings.createLayout();
		final var nodes = layout.apply(graph, new Rectangle2D.Double(0.0, 0.0, this.width, this.height));
		// Create a map of drawable nodes (wrappers for real nodes).
		final var nodesMap =
				nodes
					.stream()
					.collect(
							Collectors.toMap(
									layoutNode -> layoutNode.getNode(),
									layoutNode -> layoutNode
							)
					);
		final var edges = graph.getEdges();

		for (var edge : edges) {
			drawEdge(graphics, edge, nodesMap);
		}
		for (var node : nodes) {
			drawNode(graphics, node);
		}
	}
	
	/**
	 * Creates an image from a Refactoring Advice Graph.
	 * @param graph The graph from which to create an image.
	 * @param layoutSettings The layout settings for the Refactoring Advice Graph.
	 * @return An image of the contents of the canvas.
	 * @throws ArgumentNullException Thrown if graph or layoutSettings is null.
	 */
	public Image createImage(Graph graph, GraphLayoutSettings layoutSettings)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(layoutSettings, "layoutSettings");
		final var image = new BufferedImage((int)this.width, (int)this.height, BufferedImage.TYPE_INT_ARGB);
		final var graphics = image.createGraphics();
		this.draw(graph, graphics, layoutSettings);
		return image;
	}
	
	private static void drawEdge(
			Graphics2D graphics,
			GraphEdge edge,
			Map<GraphNode, GraphLayoutNode> nodesMap) {
		final var sourceNode = nodesMap.get(edge.getSourceNode());
		final var sourceNodeLocation = sourceNode.getLocation();
		final var destinationNode = nodesMap.get(edge.getDestinationNode());
		final var destinationNodeLocation = destinationNode.getLocation();
		
		// Line
		graphics.setColor(Color.cyan);
		graphics.drawLine(
				(int)sourceNodeLocation.x,
				(int)sourceNodeLocation.y,
				(int)destinationNodeLocation.x,
				(int)destinationNodeLocation.y);
		
		// Label
		final var labelX = (destinationNodeLocation.x + sourceNodeLocation.x) / 2;
		final var labelY = (destinationNodeLocation.y + sourceNodeLocation.y) / 2;
		final var fontMetrics = graphics.getFontMetrics(FONT_EDGE);
		final var label = edge.getLabel();
		final var labelWidth = fontMetrics.stringWidth(label);
		final var labelHeight = fontMetrics.getHeight();
		graphics.setFont(FONT_EDGE);
		graphics.drawString(label, (int)labelX - (labelWidth / 2), (int)labelY + fontMetrics.getAscent() / 2);
		
		// Arrow
		final var edgeLength = sourceNodeLocation.distance(destinationNodeLocation);
		final var arrowLocationRadius = Math.min(Math.max(labelWidth + 10, labelHeight + 10), edgeLength / 2);
		final var arrowAngle =
				Math.atan2(
						destinationNodeLocation.y - sourceNodeLocation.y,
						destinationNodeLocation.x - sourceNodeLocation.x);
		final var arrowWidth = 15;
		final var arrowHeight = 7;
		
		final var arrowTipX = labelX + Math.cos(arrowAngle) * arrowLocationRadius;
		final var arrowTipY = labelY + Math.sin(arrowAngle) * arrowLocationRadius;
		final var arrowPerpendicularX = -Math.sin(arrowAngle);
		final var arrowPerpendicularY = Math.cos(arrowAngle);
		final var arrowBaseCentreX = arrowTipX - Math.cos(arrowAngle) * arrowHeight;
		final var arrowBaseCentreY = arrowTipY - Math.sin(arrowAngle) * arrowHeight;
		final var arrowBaseLeftX = arrowBaseCentreX + arrowPerpendicularX * (arrowWidth / 2);
		final var arrowBaseLeftY = arrowBaseCentreY + arrowPerpendicularY * (arrowWidth / 2);
		final var arrowBaseRightX = arrowBaseCentreX - arrowPerpendicularX * (arrowWidth / 2);
		final var arrowBaseRightY = arrowBaseCentreY - arrowPerpendicularY * (arrowWidth / 2);
		
		final var arrowXs =
				new int[] {
						(int)arrowTipX,
						(int)arrowBaseRightX,
						(int)arrowBaseLeftX
				};
		final var arrowYs =
				new int[] {
						(int)arrowTipY,
						(int)arrowBaseRightY,
						(int)arrowBaseLeftY
				};
		final var arrow = new Polygon(arrowXs, arrowYs, 3);
		graphics.fillPolygon(arrow);
	}
	
	private static void drawNode(
			Graphics2D graphics,
			GraphLayoutNode layoutNode) {
		final var location = layoutNode.getLocation();
		final var node = layoutNode.getNode();
		
		// Measurements
		final var labelFontMetrics = graphics.getFontMetrics(FONT_LABEL);
		final var label = node.getLabel();
		final var labelWidth = labelFontMetrics.stringWidth(label);
		final var labelHeight = labelFontMetrics.getHeight();
		final var captionFontMetrics = graphics.getFontMetrics(FONT_CAPTION);
		final var caption = node.getCaption();
		final var captionWidth = captionFontMetrics.stringWidth(caption);
		final var captionHeight = captionFontMetrics.getHeight();
		final var ovalWidth = Math.max(100, Math.max(labelWidth + 20, captionWidth + 20));
		final var ovalHeight = labelHeight + captionHeight + 20;
		
		// Node oval
		graphics.setColor(getFillColour(node));
		final var x = location.x - ovalWidth / 2;
		final var y = location.y - ovalHeight / 2;
		graphics.fillOval((int)x, (int)y, ovalWidth, ovalHeight);
		
		// Node label
		final var labelX = x + (ovalWidth - labelWidth) / 2;
		final var labelY = y + labelHeight + 5;
		graphics.setColor(getColour(node));
		graphics.setFont(FONT_LABEL);
		graphics.drawString(label, (int)labelX, (int)labelY);
		
		// Node caption
		final var captionX = x + (ovalWidth - captionWidth) / 2;
		final var captionY = y + labelHeight + captionHeight + 5;
		graphics.setFont(FONT_CAPTION);
		graphics.drawString(caption, (int)captionX, (int)captionY);
	}
	
	private static Color getColour(GraphNode node) {
		return switch(node) {
			case GraphNodeCode _ -> Color.white;
			case GraphNodeMicrostep _ -> Color.black;
			case GraphNodeRemedy _ -> Color.black;
			case GraphNodeRisk _ -> Color.black;
			default -> Color.black;
		};
	}
	
	private static Color getFillColour(GraphNode node) {
		return switch(node) {
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
