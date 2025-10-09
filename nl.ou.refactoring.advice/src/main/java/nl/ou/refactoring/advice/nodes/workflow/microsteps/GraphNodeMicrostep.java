package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeCauses;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeObsolesces;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflowAction;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * Represents a microstep in a Refactoring Advice Graph.
 */
public abstract class GraphNodeMicrostep extends GraphNodeWorkflowAction {
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
}