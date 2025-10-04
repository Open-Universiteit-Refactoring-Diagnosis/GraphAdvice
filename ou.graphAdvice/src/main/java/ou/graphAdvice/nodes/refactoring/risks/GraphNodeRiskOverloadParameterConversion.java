package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.refactoring.GraphEdgeAffects;
import ou.graphAdvice.nodes.code.GraphNodeOperation;

/**
 * Represents an "Overload Parameter Conversion" risk in a Refactoring Advice Graph.
 * This risk may arise if a new method overloads an existing method and precedes the overloaded method in terms of its parameters.
 */
public final class GraphNodeRiskOverloadParameterConversion extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskOverloadParameterConversion}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskOverloadParameterConversion(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Overload Parameter Conversion" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Overload Parameter Conversion" risk and the affected Operation.
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
