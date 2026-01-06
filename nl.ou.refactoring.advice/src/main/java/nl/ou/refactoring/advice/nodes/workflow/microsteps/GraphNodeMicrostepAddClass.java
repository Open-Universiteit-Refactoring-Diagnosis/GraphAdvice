package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Class.
 */
public final class GraphNodeMicrostepAddClass extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddClass}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddClass(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that this "Add Class" microstep adds the classNode.
	 * @param classNode The node that represents the class that is added to the code context.
	 * @return The edge that indicates that this microstep adds the classNode.
	 * @throws ArgumentNullException Thrown if classNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeClass classNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(classNode, "classNode");
		return new GraphEdgeAdds(this, classNode);
	}
}