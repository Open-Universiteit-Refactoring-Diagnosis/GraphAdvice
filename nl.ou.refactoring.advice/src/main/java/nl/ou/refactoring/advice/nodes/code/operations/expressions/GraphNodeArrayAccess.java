package nl.ou.refactoring.advice.nodes.code.operations.expressions;

/**
 * A node in a Refactoring Advice Graph that represents an array access expression.
 */
public interface GraphNodeArrayAccess
		extends
			GraphNodeLeftHandSide,
			GraphNodePrimaryNoNewArrayExpression {
}