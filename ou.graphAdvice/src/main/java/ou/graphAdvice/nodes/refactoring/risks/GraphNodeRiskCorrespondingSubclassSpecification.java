package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a "Corresponding Subclass Specification risk in a Refactoring Advice Graph.
 * This risk may arise if a method is added and a method with the same signature exists in one or more subclasses.
 */
public final class GraphNodeRiskCorrespondingSubclassSpecification extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskCorrespondingSubclassSpecification}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskCorrespondingSubclassSpecification(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}
