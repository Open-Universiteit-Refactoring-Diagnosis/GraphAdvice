package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * A node in a Refactoring Advice Graph that represents a field access expression.
 */
public interface GraphNodeFieldAccess
	extends
		GraphNodeLeftHandSide,
		GraphNodePrimaryNoNewArrayExpression {
	/**
	 * Gets the node that represents the identifier of the field that is accessed.
	 * @return The node that represents the identifier of the field that is accessed.
	 */
	public GraphNodeIdentifier getIdentifier();
}