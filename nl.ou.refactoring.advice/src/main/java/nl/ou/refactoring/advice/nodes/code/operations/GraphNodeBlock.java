package nl.ou.refactoring.advice.nodes.code.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.operations.statements.GraphNodeStatement;

/**
 * A node in a Refactoring Advice Graph that represents a code block.
 */
public class GraphNodeBlock extends GraphNodeCode {
	/**
	 * Initialises a new instance of {@link GraphNodeBlock}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeBlock(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the Block node contains a statement represented by statementNode.
	 * @param statementNode The node that represents the statement inside this block.
	 * @return The edge that connects the Block and the Statement.
	 * @throws ArgumentNullException Thrown if statementNode is null.
	 */
	public GraphEdgeHas has(GraphNodeStatement statementNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(statementNode, "statementNode");
		return this.graph.getOrAddEdge(
				this,
				statementNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
	}
	
	/**
	 * Attempts to get an operation node associated with this class, indicating that the operation contains the code block as its body.
	 * @return The operation node associated with this code block.
	 * @throws NoSuchElementException Thrown if no {@link GraphNodeOperation} is associated with the block.
	 */
	public GraphNodeOperation getOperationNode() throws NoSuchElementException {
		return
			this
				.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> node instanceof GraphNodeOperation)
				.map(GraphNodeOperation.class::cast)
				.findAny()
				.orElseThrow();
	}
	
	/**
	 * Gets the statements inside the block that is represented by this node.
	 * @return An unmodifiable list of {@link GraphNodeStatement}.
	 */
	public List<GraphNodeStatement> getStatements() {
		final var statementsList = new ArrayList<GraphNodeStatement>();
		var statementNext =
			this
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeStatement)
				.map(GraphNodeStatement.class::cast)
				.findAny()
				.orElse(null);
		while (statementNext != null) {
			statementsList.add(statementNext);
			statementNext = statementNext.getNext();
		}
		return Collections.unmodifiableList(statementsList);
	}

	@Override
	public String getLabel() {
		return "Block";
	}
}
