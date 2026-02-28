package nl.ou.refactoring.advice.nodes.code.operations.statements;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;

/**
 * A node in a Refactoring Advice Graph that represents a statement that contains an expression.
 */
public final class GraphNodeExpressionStatement extends GraphNodeStatement {
	private final GraphEdgeHas statementExpressionEdge;
	
	/**
	 * Initialises a new instance of {@link GraphNodeExpressionStatement}.
	 * @param graph The graph that contains the expression statement.
	 * @param statementExpressionNode The node that represents the statement's expression.
	 * @throws ArgumentNullException Thrown if graph or statementExpressionNode is null.
	 */
	public GraphNodeExpressionStatement(Graph graph, GraphNodeStatementExpression statementExpressionNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(statementExpressionNode, "statementExpressionNode");
		super(graph);
		this.statementExpressionEdge =
			this.graph.getOrAddEdge(
				this,
				statementExpressionNode,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class
			);
	}
	
	/**
	 * Gets the node that represents the statement's expression.
	 * @return The node that represents the statement's expression.
	 */
	public GraphNodeStatementExpression getExpression() {
		return (GraphNodeStatementExpression)this.statementExpressionEdge.getDestinationNode();
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeExpressionStatement(
			graph,
			(GraphNodeStatementExpression)this.statementExpressionEdge.getDestinationNode()
		);
	}
}