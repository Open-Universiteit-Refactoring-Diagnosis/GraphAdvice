package nl.ou.refactoring.advice.nodes.workflow.remedies;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeMitigates;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

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
	 * @throws ArgumentNullException Thrown if doubleDefinition is null.
	 */
	public GraphEdgeMitigates mitigates(GraphNodeRiskDoubleDefinition doubleDefinition)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				doubleDefinition,
				(source, destination) -> new GraphEdgeMitigates(source, destination),
				GraphEdgeMitigates.class);
	}

	@Override
	public String getCaption() {
		return "Choose different name";
	}
}
