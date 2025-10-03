package ou.graphAdvice.nodes.refactoring.remedies;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.refactoring.GraphEdgeMitigates;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRiskDoubleDefinition;

/**
 * Represents a remedy of choosing a different name for a code symbol to avoid a Double Definition risk.
 */
public final class GraphNodeRemedyChooseDifferentName extends GraphNodeRemedy {
	/**
	 * Initialises a new instance of {@link GraphNodeRemedyChooseDifferentName}.
	 * @param graph {@link Graph} The graph that contains the remedy.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRemedyChooseDifferentName(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the Choose Different Name remedy mitigates the Double Definition risk.
	 * @param doubleDefinition The Double Definition risk that is mitigated by this remedy.
	 * @return An edge that connects the remedy and the risk.
	 * @throws Thrown if doubleDefinition is null.
	 */
	public GraphEdgeMitigates mitigates(GraphNodeRiskDoubleDefinition doubleDefinition)
			throws ArgumentNullException {
		return this.graph.addEdge(
				this,
				doubleDefinition,
				(source, destination) -> new GraphEdgeMitigates(source, destination),
				GraphEdgeMitigates.class);
	}
}
