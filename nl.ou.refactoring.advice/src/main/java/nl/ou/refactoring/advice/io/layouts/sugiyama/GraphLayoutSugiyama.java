package nl.ou.refactoring.advice.io.layouts.sugiyama;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.layouts.GraphLayout;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutNode;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A layout of a Refactoring Advice Graph using the Sugiyama (Layered) Layout algorithm.
 */
public final class GraphLayoutSugiyama extends GraphLayout {
	private final Map<GraphNode, Integer> layerMap = new HashMap<>();
	private final Map<GraphNode, Point2D.Double> coordinates = new HashMap<>();
	private final List<List<GraphNode>> layers = new ArrayList<>();
	
	/**
	 * Initialises a new instance of {@link GraphLayoutSugiyama}.
	 * @param settings The layout settings.
	 * @throws ArgumentNullException Thrown if settings is null.
	 */
	public GraphLayoutSugiyama(GraphLayoutSugiyamaSettings settings)
			throws ArgumentNullException {
		super(settings);
	}

	@Override
	public Set<GraphLayoutNode> apply(Graph graph, Rectangle2D area)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(area, "area");
		this.assignLayers(graph);
		// this.insertTemporaryNodes(graph);
		this.minimiseCrossings(graph);
		this.assignCoordinates();
		graph.removeNodes(GraphTemporaryNode.class);
		return
				this
					.coordinates
					.keySet()
					.stream()
					.filter(node -> node.getClass() != GraphTemporaryNode.class)
					.map(
							node ->
							{
								final var layoutNode = new GraphLayoutNode(node);
								final var coordinate = this.coordinates.get(node);
								layoutNode.getLocation().setLocation(coordinate);
								return layoutNode;
							})
					.collect(Collectors.toSet());
	}
	
	@Override
	public GraphLayoutSugiyamaSettings getSettings() {
		return (GraphLayoutSugiyamaSettings)super.getSettings();
	}

	private void assignLayers(Graph graph) {
		final var sortedNodes = TopologicalSorter.sort(graph);
		for (var sortedNode : sortedNodes) {
			final var parentLayerIndex =
					graph
						.getEdgesTo(sortedNode)
						.stream()
						.map(edge -> edge.getSourceNode())
						.mapToInt(this.layerMap::get)
						.max()
						.orElse(-1);
			final var layerIndex = parentLayerIndex + 1;
			layerMap.put(sortedNode, layerIndex);
			final var layer = this.getOrCreateLayer(layerIndex);
			layer.add(sortedNode);
		}
	}
	
	private void insertTemporaryNodes(Graph graph) {
		for (var edge : graph.getEdges()) {
			var sourceNode = edge.getSourceNode();
			final var destinationNode = edge.getDestinationNode();
			final var layerSourceIndex = this.layerMap.get(sourceNode);
			final var layerDestinationIndex = this.layerMap.get(edge.getDestinationNode());
			if (layerDestinationIndex - layerSourceIndex > 1) {
				for (
						var layerIndex = layerSourceIndex + 1;
						layerIndex < layerDestinationIndex;
						layerIndex++
				) {
					final var temporaryNode = new GraphTemporaryNode(graph);
					this.layerMap.put(temporaryNode, layerIndex);
					final var layer = this.getOrCreateLayer(layerIndex);
					layer.add(temporaryNode);
					graph.getOrAddEdge(
							sourceNode,
							temporaryNode,
							(s, d) -> new GraphLayoutSugiyamaEdge(s, d),
							GraphLayoutSugiyamaEdge.class);
					sourceNode = temporaryNode;
				}
				graph.getOrAddEdge(
						sourceNode,
						destinationNode,
						(s, d) -> new GraphLayoutSugiyamaEdge(s, d),
						GraphLayoutSugiyamaEdge.class);
			}
		}
	}
	
	private void minimiseCrossings(Graph graph) {
		for (var i = 1; i < this.layers.size(); i++) {
			final var layerIndex = i;
			var layer = this.layers.get(layerIndex);
			layer.sort(
					Comparator.comparingDouble(
							node ->
							{
								var sourceNodes =
										graph
											.getEdgesTo(node)
											.stream()
											.map(edge -> edge.getSourceNode())
											.collect(Collectors.toSet());
								return
										sourceNodes
											.stream()
											.mapToInt(index -> this.layers.get(layerIndex - 1).indexOf(index))
											.average()
											.orElse(0.0);
							}
					)
			);
		}
	}
	
	private void assignCoordinates() {
		final var settings = this.getSettings();
		final var layerHeight = settings.getLayerHeight();
		final var nodeSpacing = settings.getNodeSpacing();
		for (var i = 0; i < this.layers.size(); i++) {
			final var layer = layers.get(i);
			for (var j = 0; j < layer.size(); j++) {
				var node = layer.get(j);
				this.coordinates.put(node, new Point2D.Double(500 + j * nodeSpacing, 500 + i * layerHeight));
			}
		}
	}
	
	private List<GraphNode> getOrCreateLayer(int layer) {
		while (this.layers.size() <= layer) {
			this.layers.add(new ArrayList<>());
		}
		return layers.get(layer);
	}
}
