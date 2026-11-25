package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * Represents a "Forced Override" risk in a Refactoring Advice Graph.
 * This risk may arise if a method is defined in a superclass
 * and adding a method with the same definition will introduce an override.
 */
public final class GraphNodeDangerForcedOverride extends GraphNodeDanger {
	/**
	 * Initialises a new instance of {@link GraphNodeDangerForcedOverride}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeDangerForcedOverride(Graph graph) throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Forced Override" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Forced Override" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeDangerForcedOverride(graph);
	}
}