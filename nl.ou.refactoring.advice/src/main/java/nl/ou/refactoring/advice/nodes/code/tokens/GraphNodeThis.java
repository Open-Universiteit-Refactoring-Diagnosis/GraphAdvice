package nl.ou.refactoring.advice.nodes.code.tokens;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodePrimaryNoNewArrayExpression;

/**
 * A node in a Refactoring Advice Graph that represents the "this" keyword, a reference to the containing Class.
 */
public final class GraphNodeThis
		extends GraphNodeBase
		implements GraphNodePrimaryNoNewArrayExpression {
	/**
	 * Initialises a new instance of {@link GraphNodeThis}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeThis(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	@Override
	public String getLabel() {
		return "this";
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeThis(graph);
	}
}