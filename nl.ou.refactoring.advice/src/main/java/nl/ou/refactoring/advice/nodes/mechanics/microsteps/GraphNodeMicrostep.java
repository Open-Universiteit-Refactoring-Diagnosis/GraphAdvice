package nl.ou.refactoring.advice.nodes.mechanics.microsteps;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeCauses;
import nl.ou.refactoring.advice.edges.mechanics.GraphEdgeRelatesTo;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDanger;
import nl.ou.refactoring.advice.nodes.mechanics.GraphNodeMechanicAction;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Represents a microstep in a Refactoring Advice Graph.
 */
public abstract class GraphNodeMicrostep extends GraphNodeMechanicAction {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostep}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeMicrostep(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(GraphNodeMicrostep.class);
	}

	/**
	 * Indicates that a microstep causes a risk.
	 * 
	 * @param risk The risk that is caused by the microstep.
	 * @return An edge {@link GraphEdgeCauses} that is connected to this node and
	 *         the risk.
	 * @throws ArgumentNullException Thrown if risk is null.
	 */
	public GraphEdgeCauses causes(GraphNodeDanger risk) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				risk,
				(source, destination) -> new GraphEdgeCauses(source, destination),
				GraphEdgeCauses.class);
	}

	/**
	 * Indicates that this {@link GraphNodeMicrostep} directly relates to the
	 * specified microstep.
	 * 
	 * @param microstep The microstep to which this {@link GraphNodeMicrostep}
	 *                  relates.
	 * @return The edge of the new relationship.
	 * @throws ArgumentNullException Thrown if microstep is null.
	 */
	public GraphEdgeRelatesTo relatesTo(GraphNodeMicrostep microstep) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				microstep,
				(source, destination) -> new GraphEdgeRelatesTo(source, destination),
				GraphEdgeRelatesTo.class);
	}

	/**
	 * Gets the dangers associated with this microstep.
	 * 
	 * @return The dangers associated with this microstep in an unmodifiable
	 *         {@link Set<GraphNodeDanger>}.
	 */
	public Set<GraphNodeDanger> getDangers() {
		return this.getEdges(GraphEdgeCauses.class)
				.stream()
				.map(GraphEdgeCauses::getDestinationNode)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * Gets the {@link GraphNodeMicrostep} that relate to this microstep.
	 * 
	 * @return An unmodifiable set of {@link GraphNodeMicrostep} that relate to this
	 *         microstep.
	 */
	public Set<GraphNodeMicrostep> getRelated() {
		return Stream
				.concat(
						this.getEdges(GraphEdgeRelatesTo.class).stream().map(GraphEdgeRelatesTo::getDestinationNode),
						this.getEdgesIncoming(GraphEdgeRelatesTo.class).stream().map(GraphEdgeRelatesTo::getSourceNode))
				.collect(Collectors.toUnmodifiableSet());
	}
}