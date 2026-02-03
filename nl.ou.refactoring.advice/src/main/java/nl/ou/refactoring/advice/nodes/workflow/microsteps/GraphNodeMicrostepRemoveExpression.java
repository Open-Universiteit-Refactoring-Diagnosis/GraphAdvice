package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeStatementExpression;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes an Expression.
 */
public final class GraphNodeMicrostepRemoveExpression extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveExpression}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveExpression(Graph graph) throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the Remove Expression microstep represented by this node 
	 * removes the Statement Expression represented by {@link GraphNodeStatementExpression} statementExpressionNode.
	 * @param statementExpressionNode The node that represents the Statement Expression that is removed by this microstep.
	 * @return {@link GraphEdgeRemoves} The edge that indicates that the Statement Expression is removed by the microstep.
	 * @throws ArgumentNullException Thrown if statementExpressionNode is null.
	 */
	public GraphEdgeRemoves removes(GraphNodeStatementExpression statementExpressionNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(statementExpressionNode, "statementExpressionNode");
		return
			this
				.graph
				.getOrAddEdge(
					this,
					statementExpressionNode,
					(sourceNode, destinationNode) -> new GraphEdgeRemoves(sourceNode, destinationNode),
					GraphEdgeRemoves.class
				);
	}
}