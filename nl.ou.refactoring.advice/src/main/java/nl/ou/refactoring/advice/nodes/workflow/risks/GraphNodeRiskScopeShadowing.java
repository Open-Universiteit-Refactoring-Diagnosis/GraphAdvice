package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;

/**
 * Represents a "Scope Shadowing" risk in a Refactoring Advice Graph.
 * This risk may arise if a field to add is also defined in an ancestor class.
 * Adding the field will hide the field in the ancestor class, causing existing accessors
 * to use the local definition instead of the ancestor's definition.
 */
public class GraphNodeRiskScopeShadowing extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskScopeShadowing}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskScopeShadowing(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the "Scope Shadowing" affects an Attribute.
	 * @param attributeNode The affected Attribute.
	 * @return The edge that connects the "Scope Shadowing" risk and the affected Attribute.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				attributeNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
