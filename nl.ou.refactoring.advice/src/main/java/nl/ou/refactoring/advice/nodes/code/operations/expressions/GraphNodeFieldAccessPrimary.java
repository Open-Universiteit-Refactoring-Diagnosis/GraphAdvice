package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * A node in a Refactoring Advice Graph that represents a field access expression.
 */
public final class GraphNodeFieldAccessPrimary extends GraphNodeBase implements GraphNodeFieldAccess {
	/**
	 * The "has" relationship with the node that represents the primary expression.
	 */
	private final GraphEdgeHas primaryExpressionEdge;
	
	/**
	 * The "has" relationship with the node that represents the identifier of the field that is accessed.
	 */
	private final GraphEdgeHas identifierEdge;

	/**
	 * Initialises a new instance of {@link GraphNodeFieldAccessPrimary}.
	 * @param graph The graph that contains the field access expression node.
	 * @param primaryExpression A node that represents the primary expression.
	 * @param identifier A node that represents the identifier of the field.
	 * @throws ArgumentNullException Thrown if graph, primaryExpression or identifier is null.
	 */
	public GraphNodeFieldAccessPrimary(
		Graph graph,
		GraphNodePrimaryExpression primaryExpression,
		GraphNodeIdentifier identifier
	)
		throws ArgumentNullException
	{
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(primaryExpression, "primaryExpression");
		ArgumentGuard.requireNotNull(identifier, "identifier");
		super(graph);
		this.primaryExpressionEdge =
			this.graph.getOrAddEdge(
				this,
				primaryExpression,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class
			);
		this.identifierEdge =
			this.graph.getOrAddEdge(
				this,
				identifier,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class
			);
	}
	
	/**
	 * Gets the node that represents the primary expression of the field access.
	 * @return The node that represents the primary expression of the field access.
	 */
	public GraphNodePrimaryExpression getPrimaryExpression() {
		return (GraphNodePrimaryExpression)this.primaryExpressionEdge.getDestinationNode();
	}
	
	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeFieldAccessPrimary(
			graph,
			(GraphNodePrimaryExpression)this.primaryExpressionEdge.getDestinationNode().clone(graph),
			(GraphNodeIdentifier)this.identifierEdge.getDestinationNode().clone(graph)
		);
	}
	
	@Override
	public GraphNodeIdentifier getIdentifier() {
		return (GraphNodeIdentifier)this.identifierEdge.getDestinationNode();
	}

	@Override
	public String getLabel() {
		return "Field Access";
	}
	
	@Override
	public String toString() {
		return this.getPrimaryExpression().toString() + "." + this.getIdentifier().toString();
	}
}