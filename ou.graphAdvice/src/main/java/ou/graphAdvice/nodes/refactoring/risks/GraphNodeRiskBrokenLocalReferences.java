package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

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
}