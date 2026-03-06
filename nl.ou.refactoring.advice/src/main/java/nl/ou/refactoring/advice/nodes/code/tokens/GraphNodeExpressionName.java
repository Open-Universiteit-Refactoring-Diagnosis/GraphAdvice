package nl.ou.refactoring.advice.nodes.code.tokens;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeReferences;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeLeftHandSide;

/**
 * A name expression.
 */
public interface GraphNodeExpressionName extends GraphNodeLeftHandSide {
	/**
	 * Indicates that the name expression references a field (represented by {@link GraphNodeAttribute}).
	 * @param attributeNode The node that represents the referenced field (represented by {@link GraphNodeAttribute}).
	 * @return The edge that indicates that the name expression references a field.
	 * @throws ArgumentNullException Throw if attributeNode is null.
	 */
	public GraphEdgeReferences references(GraphNodeAttribute attributeNode) throws ArgumentNullException;
}