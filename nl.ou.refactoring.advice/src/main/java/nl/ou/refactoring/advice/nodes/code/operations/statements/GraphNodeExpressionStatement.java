package nl.ou.refactoring.advice.nodes.code.operations.statements;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;

/**
 * A node in a Refactoring Advice Graph that represents a statement that contains an expression.
 */
public final class GraphNodeExpressionStatement extends GraphNodeStatement {
	/**
	 * Initialises a new instance of {@link GraphNodeExpressionStatement}.
	 * @param graph The graph that contains the expression statement.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeExpressionStatement(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the statement contains the specified expression represented by statementExpressionNode.
	 * @param statementExpressionNode A node that represents an expression in a statement.
	 * @return The edge that indicates that the statement contains an expression.
	 * @throws ArgumentNullException Thrown if statementExpressionNode is null.
	 */
	public GraphEdgeHas has(GraphNodeStatementExpression statementExpressionNode)
			throws ArgumentNullException {
		return
			this
				.graph
				.getOrAddEdge(
					this,
					statementExpressionNode,
					(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
					GraphEdgeHas.class
				);
	}

	@Override
	public GraphNode clone(Graph graph) {
		return new GraphNodeExpressionStatement(graph);
	}
}
