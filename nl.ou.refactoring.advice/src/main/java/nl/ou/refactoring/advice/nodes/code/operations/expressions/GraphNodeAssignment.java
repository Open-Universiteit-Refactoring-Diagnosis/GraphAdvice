package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;

/**
 * A node in a Refactoring Advice Graph that represents an assignment.
 */
public final class GraphNodeAssignment
	extends GraphNodeStatementExpression
	implements GraphNodeAssignmentExpression
{
	private final GraphEdgeHas leftHandSide;
	private final AssignmentOperator assignmentOperator;
	private final GraphEdgeHas assignmentExpression;
	
	/**
	 * Initialises a new instance of {@link GraphNodeAssignment}.
	 * @param graph The Refactoring Advice Graph that contains the node.
	 * @param leftHandSide The left hand side of the assignment.
	 * @param assignmentOperator The operator of the assignment.
	 * @param assignmentExpression The right hand side of the assignment.
	 * @throws ArgumentNullException Thrown if graph, leftHandSide or assignmentExpression is null.
	 */
	public GraphNodeAssignment(
		Graph graph,
		GraphNodeLeftHandSide leftHandSide,
		AssignmentOperator assignmentOperator,
		GraphNodeAssignmentExpression assignmentExpression
	) throws ArgumentNullException {
		super(graph);
		ArgumentGuard.requireNotNull(leftHandSide, "leftHandSide");
		ArgumentGuard.requireNotNull(assignmentExpression, "assignmentExpression");
		this.leftHandSide =
			graph
				.getOrAddEdge(
					this,
					leftHandSide,
					(source, destination) -> new GraphEdgeHas(source, destination),
					GraphEdgeHas.class
				);
		this.assignmentOperator = assignmentOperator;
		this.assignmentExpression =
			graph
				.getOrAddEdge(
					this,
					assignmentExpression,
					(source, destination) -> new GraphEdgeHas(source, destination),
					GraphEdgeHas.class
				);
	}

	@Override
	public GraphNodeAssignment clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeAssignment(
			graph,
			(GraphNodeLeftHandSide)this.leftHandSide.getDestinationNode().clone(graph),
			this.assignmentOperator,
			(GraphNodeAssignmentExpression)this.assignmentExpression.getDestinationNode().clone(graph)
		);
	}
}