package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Method.
 */
public final class GraphNodeMicrostepAddMethod extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddMethod}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddMethod(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Add Method" microstep adds the operation represented by operationNode.
	 * @param operationNode The operation node that is added by this microstep.
	 * @return The edge that indicates that the "Add Method" microstep adds the operation represented by operationNode.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		return new GraphEdgeAdds(this, operationNode);
	}
}
