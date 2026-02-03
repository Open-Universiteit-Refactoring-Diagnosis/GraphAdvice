package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import java.util.NoSuchElementException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeExpressionStatement;

/**
 * A node in a Refactoring Advice Graph that represents an expression in a statement.
 */
public abstract class GraphNodeStatementExpression extends GraphNodeCode {
	/**
	 * Initialises a new instance of {@link GraphNodeStatementExpression}.
	 * @param graph The Refactoring Advice Graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeStatementExpression(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the node that represents the Operation that contains the Expression.
	 * @return {@link GraphNodeOperation} The node that represents the operation that contains the Method Invocation Expression.
	 * @throws NoSuchElementException Thrown if no node that represents the Operation that contains the Expression is associated with this node.
	 */
	public GraphNodeOperation getOperationNode() throws NoSuchElementException {
		return
			this
				.getStatement()
				.getOperationNode();
	}
	
	/**
	 * Gets the node that represents the Statement that contains the Expression.
	 * @return {@link GraphNodeExpressionStatement} The node that represents the Statement that contains the Expression.
	 * @throws NoSuchElementException Thrown if no node that represents the Statement that contains the Expression is associated with this node.
	 */
	public GraphNodeExpressionStatement getStatement() throws NoSuchElementException {
		return
			this
				.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> node instanceof GraphNodeExpressionStatement)
				.map(GraphNodeExpressionStatement.class::cast)
				.findAny()
				.orElseThrow();
	}

	@Override
	public String getLabel() {
		return "Statement expression";
	}
}
