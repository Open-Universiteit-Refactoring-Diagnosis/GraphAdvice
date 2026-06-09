package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeCauses;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRelatesTo;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflowAction;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMustContainStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Represents a microstep in a Refactoring Advice Graph.
 */
public abstract class GraphNodeMicrostep extends GraphNodeWorkflowAction {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostep}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeMicrostep(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that a microstep causes a risk.
	 * 
	 * @param risk The risk that is caused by the microstep.
	 * @return An edge {@link GraphEdgeCauses} that is connected to this node and
	 *         the risk.
	 * @throws ArgumentNullException Thrown if risk is null.
	 */
	public GraphEdgeCauses causes(GraphNodeRisk risk) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				risk,
				(source, destination) -> new GraphEdgeCauses(source, destination),
				GraphEdgeCauses.class);
	}

	/**
	 * Finalises the refactoring.
	 * 
	 * @return An edge {@link GraphEdgeFinalises} that finalises the refactoring.
	 */
	public GraphEdgeFinalises finalises() throws RefactoringMustContainStartNodeException {
		final var startNodeOptional = this.graph.getStart();
		if (startNodeOptional.isEmpty()) {
			throw new RefactoringMustContainStartNodeException();
		}
		return this.graph.computeEdge(
				this,
				startNodeOptional.get(),
				(source, destination) -> new GraphEdgeFinalises(source, destination),
				GraphEdgeFinalises.class);
	}

	/**
	 * Obsolesces a risk caused by another microstep.
	 * 
	 * @param risk The risk that is becoming obsolete.
	 * @return An edge {@link GraphEdgeObsolesces} that is connected to this
	 *         microstep and the obsolete risk.
	 * @throws ArgumentNullException Thrown if risk is null.
	 */
	public GraphEdgeObsolesces obsolesces(GraphNodeRisk risk) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				risk,
				(source, destination) -> new GraphEdgeObsolesces(source, destination),
				GraphEdgeObsolesces.class);
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
	 * Gets the risks associated with this microstep.
	 * 
	 * @return The risks associated with this microstep in an unmodifiable
	 *         {@link Set<GraphNodeRisk>}.
	 */
	public Set<GraphNodeRisk> getRisks() {
		return this.getEdges(GraphEdgeCauses.class)
				.stream()
				.map(GraphEdgeCauses::getDestinationNode)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * Gets the dangers associated with this microstep.
	 * 
	 * @return The dangers associated with this microstep.
	 */
	public Set<GraphNodeRisk> getDangers() {
		return this.getRisks()
				.stream()
				// TODO determine whether the risk really has been neutralised
				.filter(node -> node.getNeutralisers().isEmpty())
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

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(GraphNodeMicrostep.class);
	}
}