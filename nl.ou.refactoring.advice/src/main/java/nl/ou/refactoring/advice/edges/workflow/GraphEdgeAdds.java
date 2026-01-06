package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

/**
 * An edge in a Refactoring Advice Graph that indicates that a microstep adds a new code symbol.
 */
public final class GraphEdgeAdds extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeAdds}.
	 * @param sourceNode The source "Add Class" microstep.
	 * @param destinationNode The destination "Class" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeAdds(GraphNodeMicrostepAddClass sourceNode, GraphNodeClass destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAdds}.
	 * @param sourceNode The source "Add Field" microstep.
	 * @param destinationNode The destination "Attribute" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeAdds(GraphNodeMicrostepAddField sourceNode, GraphNodeAttribute destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeAdds}.
	 * @param sourceNode The source "Add Method" microstep.
	 * @param destinationNode The destination "Operation" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeAdds(GraphNodeMicrostepAddMethod sourceNode, GraphNodeOperation destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}

	@Override
	public String getLabel() {
		return "Adds";
	}
}
