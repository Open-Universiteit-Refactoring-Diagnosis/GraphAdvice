package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

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
}
