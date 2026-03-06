package nl.ou.refactoring.advice.edges.code.operations.expressions;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeExpressionName;

/**
 * An edge in a Refactoring Advice Graph that represents a reference of a code symbol to another code symbol.
 */
public final class GraphEdgeReferences extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeReferences}.
	 * @param sourceNode The node that references a field ({@link GraphNodeAttribute}).
	 * @param destinationNode The node that represents the referenced field ({@link GraphNodeAttribute}).
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is null.
	 */
	public GraphEdgeReferences(GraphNodeExpressionName sourceNode, GraphNodeAttribute destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}
}