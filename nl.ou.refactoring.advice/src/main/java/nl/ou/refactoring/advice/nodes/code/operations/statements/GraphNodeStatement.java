package nl.ou.refactoring.advice.nodes.code.operations.statements;

import java.util.NoSuchElementException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeList;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeBlock;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * A node in a Refactoring Advice Graph that represents a statement inside an operation.
 */
public abstract class GraphNodeStatement extends GraphNodeCode {
	/**
	 * Initialises a new instance of {@link GraphNodeStatement}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeStatement(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the next statement after this statement, if any.
	 * @return The next statement, if any, otherwise null.
	 */
	public final GraphNodeStatement getNext() {
		return
			this
				.getEdges(GraphEdgeList.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeStatement)
				.map(GraphNodeStatement.class::cast)
				.findAny()
				.orElse(null);
	}
	
	/**
	 * Gets the previous statement before this statement, if any.
	 * @return The previous statement, if any, otherwise null.
	 */
	public final GraphNodeStatement getPrevious() {
		return
			this
				.getEdgesIncoming(GraphEdgeList.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> node instanceof GraphNodeStatement)
				.map(GraphNodeStatement.class::cast)
				.findAny()
				.orElse(null);
	}
	
	/**
	 * Gets the node that represents the code block that contains the statement.
	 * @return {@link GraphNodeBlock} The node that represents the code block that contains the statement.
	 * @throws NoSuchElementException Thrown if no {@link GraphNodeBlock} is associated with the statement.
	 */
	public final GraphNodeBlock getBlock() throws NoSuchElementException {
		var statementNode = this;
		var statementNodePrevious = statementNode.getPrevious();
		while (statementNodePrevious != null) {
			statementNodePrevious = statementNode.getPrevious();
			if (statementNodePrevious != null) {
				statementNode = statementNodePrevious;
			}
		}
		
		return
			statementNode
				.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> node instanceof GraphNodeBlock)
				.map(GraphNodeBlock.class::cast)
				.findAny()
				.orElseThrow();
	}
	
	/**
	 * Gets the node that represents the operation that contains the statement.
	 * @return {@link GraphNodeOperation} The node that represents the operation that contains the statement.
	 * @throws NoSuchElementException Thrown if no {@link GraphNodeOperation} is associated with the statement.
	 */
	public final GraphNodeOperation getOperationNode() throws NoSuchElementException {
		return
			this
				.getBlock()
				.getOperationNode();
	}
	
	/**
	 * Indicates the next statement in a statements list with the current node as the head
	 * in the current position of the list.
	 * @param next The next statement in the statements list.
	 * @return An edge that indicates that the current statement node and the next statement node are a list of statement nodes.
	 * @throws ArgumentNullException Thrown if next is null.
	 */
	public GraphEdgeList hasNext(GraphNodeStatement next)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(next, "next");
		return
			this
				.graph
				.getOrAddEdge(
						this,
						next,
						(source, destination) -> new GraphEdgeList(source, destination),
						GraphEdgeList.class
				);
	}

	@Override
	public String getLabel() {
		return "Statement";
	}
}
