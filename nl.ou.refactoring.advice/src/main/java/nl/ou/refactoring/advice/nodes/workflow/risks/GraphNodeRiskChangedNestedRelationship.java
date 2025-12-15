package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a "Changed Nested Relationship" risk in a Refactoring Advice Graph.
 * This risk may arise if a method that is added will cause callers in an inner class
 * to call the method in the inner class instead.
 */
public class GraphNodeRiskChangedNestedRelationship extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskChangedNestedRelationship}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskChangedNestedRelationship(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}