package nl.ou.refactoring.advice.edges.code;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperationParameter;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeStatement;

/**
 * Represents an edge that connects a list of nodes.
 */
public final class GraphEdgeList extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeList}.
	 * @param head The head of the operation parameters list.
	 * @param tail The next item in the operation parameters list.
	 * @throws ArgumentNullException Thrown if head or tail is null.
	 */
	public GraphEdgeList(GraphNodeOperationParameter head, GraphNodeOperationParameter tail)
			throws ArgumentNullException {
		super(head, tail);
	}
	
	/**
	 * Initialises a new instance of {@link GraphEdgeList}. 
	 * @param head The head of the statements list.
	 * @param tail The next item in the statements list.
	 * @throws ArgumentNullException Thrown if head or tail is null.
	 */
	public GraphEdgeList(GraphNodeStatement head, GraphNodeStatement tail)
			throws ArgumentNullException {
		super(head, tail);
	}
}