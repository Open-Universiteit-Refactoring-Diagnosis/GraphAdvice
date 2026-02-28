package nl.ou.refactoring.advice.nodes.code.operations.expressions;

/**
 * A node in a Refactoring Advice Graph that represents a field access expression.
 */
public interface GraphNodeFieldAccess
	extends
		GraphNodeLeftHandSide,
		GraphNodePrimaryNoNewArrayExpression {
}