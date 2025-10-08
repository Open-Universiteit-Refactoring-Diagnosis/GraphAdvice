package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeCauses;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoring;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * Represents a microstep in a Refactoring Advice Graph.
 */
public abstract class GraphNodeMicrostep extends GraphNodeRefactoring {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostep}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeMicrostep(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	
	/**
	 * Indicates that a microstep causes a risk.
	 * @param risk The risk that is caused by the microstep.
	 * @return An edge {@link GraphEdgeCauses} that is connected to this node and the risk.
	 * @throws ArgumentNullException Thrown if risk is null.
	 */
	public GraphEdgeCauses causes(GraphNodeRisk risk)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				risk,
				(source, destination) -> new GraphEdgeCauses(source, destination),
				GraphEdgeCauses.class);
	}
	
	/**
	 * Finalises the refactoring.
	 * @return An edge {@link GraphEdgeFinalises} that finalises the refactoring.
	 */
	public GraphEdgeFinalises finalises() {
		return this.graph.getOrAddEdge(
				this,
				this.graph.getStart(),
				(source, destination) -> new GraphEdgeFinalises(source, destination),
				GraphEdgeFinalises.class);
	}
	
	/**
	 * Gets the directly preceding microstep.
	 * @return The directly preceding microstep or if it's not there, null.
	 */
	public GraphNodeMicrostep getPreceding() {
		final var edge =
				this
					.graph
					.getEdgesTo(this, GraphEdgePrecedes.class)
					.stream()
					.findAny()
					.orElse(null);
		return (GraphNodeMicrostep)(edge == null ? null : edge.getSourceNode());
	}
	
	/**
	 * Gets the length of a chain of microsteps.
	 * @param microstep The preceding microstep.
	 * @return The length of the chain of microsteps, if there is any. If there is no chain, -1 is returned.
	 */
	public int getPrecedingLength(GraphNodeMicrostep microstep) {
		final var directlyPrecedingMicrostep = this.getPreceding();
		if (directlyPrecedingMicrostep == null) {
			return -1;
		}
		if (directlyPrecedingMicrostep.equals(microstep)) {
			return 1;
		}
		final var nextLength = directlyPrecedingMicrostep.getPrecedingLength(microstep);
		if (nextLength < 0) {
			return nextLength;
		}
		return nextLength + 1;
	}
	
	/**
	 * Indicates whether this microstep is preceded by the specified microstep.
	 * @param microstep The microstep that may or may not precede the current microstep.
	 * @return True if the specified microstep precedes the current microstep, false if not.
	 */
	public boolean isPrecededBy(GraphNodeMicrostep microstep) {
		return this.getPrecedingLength(microstep) > 0;
	}
	
	/**
	 * Obsolesces a risk caused by another microstep.
	 * @param risk The risk that is becoming obsolete.
	 * @return An edge {@link GraphEdgeObsolesces} that is connected to this microstep and the obsolete risk.
	 * @throws ArgumentNullException Thrown if risk is null.
	 */
	public GraphEdgeObsolesces obsolesces(GraphNodeRisk risk)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				risk,
				(source, destination) -> new GraphEdgeObsolesces(source, destination),
				GraphEdgeObsolesces.class);
	}
	
	/**
	 * Chains a microstep to this microstep.
	 * If the relationship already exists, no edges will be added to the graph and the existing edge will be returned.
	 * @param next The next microstep in the chain.
	 * @return An edge {@link GraphEdgePrecedes} that is connected to this node and the next node.
	 * @throws ArgumentNullException Thrown if next is null.
	 */
	public GraphEdgePrecedes precedes(GraphNodeMicrostep next)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				next,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
	}
}