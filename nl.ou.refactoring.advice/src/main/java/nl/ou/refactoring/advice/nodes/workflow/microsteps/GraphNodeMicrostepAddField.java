package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Field.
 */
public final class GraphNodeMicrostepAddField extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddField}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddField(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Add Field" microstep adds the attribute represented by attributeNode.
	 * @param attributeNode The attribute node that is added by this microstep.
	 * @return The edge that indicates that the "Add Field" microstep adds the attribute represented by attributeNode.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(attributeNode, "attributeNode");
		return new GraphEdgeAdds(this, attributeNode);
	}
}
