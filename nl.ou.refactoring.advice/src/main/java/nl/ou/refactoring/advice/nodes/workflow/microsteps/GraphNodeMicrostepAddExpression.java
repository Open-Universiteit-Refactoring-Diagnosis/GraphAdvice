package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds an Expression.
 */
public final class GraphNodeMicrostepAddExpression extends GraphNodeMicrostepAdd {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddExpression}.
	 * 
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddExpression(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the microstep adds the specified statement expression.
	 * 
	 * @param statementExpressionNode The node that represents the statement
	 *                                expression that is added.
	 * @return The edge that indicates that the microstep adds the specified
	 *         statement expression.
	 * @throws ArgumentNullException Thrown if statementExpressionNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeStatementExpression statementExpressionNode) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(statementExpressionNode, "statementExpressionNode");
		return this.graph.computeEdge(
				this,
				statementExpressionNode,
				(sourceNode, destinationNode) -> new GraphEdgeAdds(sourceNode, destinationNode),
				GraphEdgeAdds.class);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepAddExpression(graph);
	}
}