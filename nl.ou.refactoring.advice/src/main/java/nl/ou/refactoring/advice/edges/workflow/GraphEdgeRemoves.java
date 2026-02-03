package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveClass;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveField;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

/**
 * An edge in a Refactoring Advice Graph that indicates that a microstep removes a code symbol.
 */
public final class GraphEdgeRemoves extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeRemoves}.
	 * @param sourceNode The source "Remove Class" microstep.
	 * @param destinationNode The destination "Class" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeRemoves(
			GraphNodeMicrostepRemoveClass sourceNode,
			GraphNodeClass destinationNode
	) throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeRemoves}.
	 * @param sourceNode The source "Remove Expression" microstep.
	 * @param destinationNode The destination "Statement Expression" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeRemoves(
			GraphNodeMicrostepRemoveExpression sourceNode,
			GraphNodeStatementExpression destinationNode
	) throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeRemoves}.
	 * @param sourceNode The source "Remove Field" microstep.
	 * @param destinationNode The destination "Attribute" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeRemoves(
			GraphNodeMicrostepRemoveField sourceNode,
			GraphNodeAttribute destinationNode
	) throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeRemoves}.
	 * @param sourceNode The source "Remove Method" microstep.
	 * @param destinationNode The destination "Operation" code node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeRemoves(
			GraphNodeMicrostepRemoveMethod sourceNode,
			GraphNodeOperation destinationNode
	) throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
}
