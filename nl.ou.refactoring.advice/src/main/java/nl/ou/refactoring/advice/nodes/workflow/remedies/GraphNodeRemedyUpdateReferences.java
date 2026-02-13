package nl.ou.refactoring.advice.nodes.workflow.remedies;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeMitigates;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

/**
 * Represents a remedy of updating references to a code symbol that may have been moved or renamed.
 */
public final class GraphNodeRemedyUpdateReferences extends GraphNodeRemedy {
	/**
	 * Initialises a new instance of {@link GraphNodeRemedyUpdateReferences}.
	 * @param graph {@link Graph} The graph that contains the remedy.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRemedyUpdateReferences(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the Update References remedy mitigates the Missing Definition risk.
	 * @param missingDefinition The Missing Definition risk that is mitigated by this remedy.
	 * @return An edge that connects the remedy and the risk.
	 * @throws ArgumentNullException Thrown if missingDefinition is null.
	 */
	public GraphEdgeMitigates mitigates(GraphNodeRiskMissingDefinition missingDefinition)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				missingDefinition,
				(source, destination) -> new GraphEdgeMitigates(source, destination),
				GraphEdgeMitigates.class);
	}
}
