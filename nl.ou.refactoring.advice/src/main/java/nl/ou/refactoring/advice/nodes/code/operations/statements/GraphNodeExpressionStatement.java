package nl.ou.refactoring.advice.nodes.code.operations.statements;

import java.util.NoSuchElementException;

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
	/**
	 * Initialises a new instance of {@link GraphNodeExpressionStatement}.
	 * @param graph The graph that contains the expression statement.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeExpressionStatement(Graph graph)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		super(graph);
	}
	
	/**
	 * Gets the node that represents the statement's expression.
	 * @return The node that represents the statement's expression.
	 * @throws NoSuchElementException Thrown if no statement expression is associated with this expression statement.
	 */
	public GraphNodeStatementExpression getExpression() throws NoSuchElementException {
		return
			this
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map((edge) -> edge.getDestinationNode())
				.filter(GraphNodeStatementExpression.class::isInstance)
				.map(GraphNodeStatementExpression.class::cast)
				.findAny()
				.orElseThrow();
	}
	
	/**
	 * Indicates that the expression statement has the specified statement expression.
	 * @param statementExpressionNode The statement expression node of the expression statement.
	 * @return The edge that indicates that the expression statement has the specified statement expression.
	 */
	public GraphEdgeHas has(GraphNodeStatementExpression statementExpressionNode) {
		return this.graph.computeEdge(
			this,
			statementExpressionNode,
			(source, destination) -> new GraphEdgeHas(source, destination),
			GraphEdgeHas.class
		);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeExpressionStatement(graph);
	}
}