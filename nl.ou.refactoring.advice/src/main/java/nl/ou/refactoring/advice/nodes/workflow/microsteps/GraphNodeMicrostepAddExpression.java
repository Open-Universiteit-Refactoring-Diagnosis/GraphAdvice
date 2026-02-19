package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds an Expression.
 */
public final class GraphNodeMicrostepAddExpression extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddExpression}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddExpression(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	public GraphEdgeAdds adds(GraphNodeStatementExpression statementExpressionNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(statementExpressionNode, "statementExpressionNode");
		return
			this
				.graph
				.getOrAddEdge(
					this,
					statementExpressionNode,
					(sourceNode, destinationNode) -> new GraphEdgeAdds(sourceNode, destinationNode),
					GraphEdgeAdds.class
				);
	}

	@Override
	public GraphNode clone(Graph graph) {
		return new GraphNodeMicrostepAddExpression(graph);
	}
}