package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.refactoring.GraphEdgeAffects;
import ou.graphAdvice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Broken Local References" risk in a Refactoring Advice Graph.
 * This risk may arise if a method body contains references in the programme code that are no longer accessible after a microstep.
 */
public class GraphNodeRiskBrokenLocalReferences extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskBrokenLocalReferences}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskBrokenLocalReferences(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Broken Local References" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Broken Local References" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.addEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}