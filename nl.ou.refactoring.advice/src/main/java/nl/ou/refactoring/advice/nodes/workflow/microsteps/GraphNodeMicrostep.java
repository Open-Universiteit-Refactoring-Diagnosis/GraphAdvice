package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeCauses;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeFinalises;
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
		return this.graph.addEdge(
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
		return this.graph.addEdge(
				this,
				this.graph.getStart(),
				(source, destination) -> new GraphEdgeFinalises(source, destination),
				GraphEdgeFinalises.class);
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
		return this.graph.addEdge(
				this,
				next,
				(source, destination) -> new GraphEdgePrecedes(source, destination),
				GraphEdgePrecedes.class);
	}
}