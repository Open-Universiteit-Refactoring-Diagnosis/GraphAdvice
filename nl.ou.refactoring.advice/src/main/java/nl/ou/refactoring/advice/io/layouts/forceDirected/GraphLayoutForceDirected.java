package nl.ou.refactoring.advice.io.layouts.forceDirected;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.io.layouts.GraphLayout;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutNode;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;

/**
 * A layout of a Refactoring Advice Graph using the Force-Directed Layout algorithm.
 */
public final class GraphLayoutForceDirected extends GraphLayout {
	/**
	 * Initialises a new instance of {@link GraphLayoutForceDirected}.
	 * @param settings The layout settings.
	 * @throws ArgumentNullException Thrown if settings is null.
	 */
	public GraphLayoutForceDirected(GraphLayoutForceDirectedSettings settings)
			throws ArgumentNullException {
		super(settings);
	}

	@Override
	public Set<GraphLayoutNode> apply(Graph graph, Rectangle2D area) {
		final var nodesMap =
				graph
					.getNodes()
					.stream()
					.collect(Collectors.toMap(node -> node, node -> new GraphLayoutForceDirectedNode(node)));
		final var nodes =
				nodesMap
					.values()
					.stream()
					.collect(Collectors.toSet());
		final var edges = graph.getEdges();
		
		// Initialise nodes in centre.
		final var centreX = area.getCenterX();
		final var centreY = area.getCenterY();
		for (var node : nodes) {
			if (node.getNode().getClass() == GraphNodeRefactoringStart.class) {
				node.getLocation().setLocation(200, centreY);
				continue;
			}
			node
				.getLocation()
				.setLocation(
						centreX + new Random().nextDouble(10),
						centreY + new Random().nextDouble(10));
		}
		final var settings = this.getSettings();
		for (var i = 0; i < settings.getIterations(); i++) {
			applyRepulsion(
					nodes,
					settings.getRepulsionConstant());
			applyAttraction(
					nodesMap,
					edges,
					settings.getSpringLength(),
					settings.getSpringStrength());
			updatePositions(
					nodes,
					settings.getTimeStep(),
					settings.getDamping());
		}
		return
				nodes
					.stream()
					.map(GraphLayoutNode.class::cast)
					.collect(Collectors.toSet());
	}
	
	@Override
	public GraphLayoutForceDirectedSettings getSettings() {
		return (GraphLayoutForceDirectedSettings)super.getSettings();
	}
	
	private static void applyRepulsion(
			Set<GraphLayoutForceDirectedNode> nodes,
			double repulsionConstant) {
		for (var node : nodes) {
			node.getForce().setLocation(0, 0);
			for (var nodeReference : nodes) {
				if (node == nodeReference) {
					continue;
				}
				final var differenceX = node.getLocation().x - nodeReference.getLocation().x;
				final var differenceY = node.getLocation().y - nodeReference.getLocation().y;
				final var distance = Math.max(1,  Math.hypot(differenceX, differenceY));
				final var repulsion = repulsionConstant / (distance * distance);
				node.getForce().x += (differenceX / distance) * repulsion;
				node.getForce().y += (differenceY / distance) * repulsion;
			}
		}
	}
	
	private static void applyAttraction(
			Map<GraphNode, GraphLayoutForceDirectedNode> nodes,
			Set<GraphEdge> edges,
			double springLength,
			double springStrength) {
		for (var edge : edges) {
			final var graphicsNodeSource = nodes.get(edge.getSourceNode());
			final var graphicsNodeDestination = nodes.get(edge.getDestinationNode());
			final var differenceX = graphicsNodeDestination.getLocation().x - graphicsNodeSource.getLocation().x;
			final var differenceY = graphicsNodeDestination.getLocation().y - graphicsNodeSource.getLocation().y;
			final var distance = Math.max(1,  Math.hypot(differenceX, differenceY));
			final var displacement = distance - springLength;
			final var force = springStrength * displacement;
			final var forceX = (differenceX / distance) * force;
			final var forceY = (differenceY / distance) * force;
			
			graphicsNodeSource.getForce().x += forceX;
			graphicsNodeSource.getForce().y += forceY;
			graphicsNodeDestination.getForce().x -= forceX;
			graphicsNodeDestination.getForce().y -= forceY;
		}
	}
	
	private static void updatePositions(
			Set<GraphLayoutForceDirectedNode> nodes,
			double timeStep,
			double damping) {
		for (var node : nodes) {
			if (node.getNode().getClass() == GraphNodeRefactoringStart.class) {
				continue;
			}
			node.getVelocity().x = (node.getVelocity().x + node.getForce().x * timeStep) * damping;
			node.getVelocity().y = (node.getVelocity().y + node.getForce().y * timeStep) * damping;
			node.getLocation().x += node.getVelocity().x * timeStep;
			node.getLocation().y += node.getVelocity().y * timeStep;
		}
	}
}
