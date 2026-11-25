package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * Represents a "Broken Local References" risk in a Refactoring Advice Graph.
 * This risk may arise if a method body contains references in the programme
 * code that are no longer accessible after a microstep.
 */
public final class GraphNodeDangerBrokenLocalReferences extends GraphNodeDanger {
	/**
	 * Initialises a new instance of {@link GraphNodeDangerBrokenLocalReferences}.
	 * 
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeDangerBrokenLocalReferences(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the "Broken Local References" risk affects an Operation.
	 * 
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Broken Local References" risk and the
	 *         affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeDangerBrokenLocalReferences(graph);
	}
}