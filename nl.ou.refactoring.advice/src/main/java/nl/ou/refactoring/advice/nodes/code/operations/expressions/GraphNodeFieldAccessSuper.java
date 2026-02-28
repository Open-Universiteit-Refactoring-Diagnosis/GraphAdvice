package nl.ou.refactoring.advice.nodes.code.operations.expressions;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * A node in a Refactoring Advice Graph that represents a field access expression with a super Class.
 */
public class GraphNodeFieldAccessSuper extends GraphNodeBase implements GraphNodeFieldAccess {
	/**
	 * The "has" relationship with the node that represents the identifier of the field that is accessed.
	 */
	private final GraphEdgeHas identifierEdge;
	
	/**
	 * Initialises a new instance of {@link GraphNodeFieldAccessSuper}.
	 * @param graph The graph that contains the node.
	 * @param identifier The node that represents the identifier of the field that is accessed.
	 * @throws ArgumentNullException Thrown if graph or identifier is null.
	 */
	public GraphNodeFieldAccessSuper(Graph graph, GraphNodeIdentifier identifier)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(identifier, "identifier");
		super(graph);
		this.identifierEdge =
			this.graph.getOrAddEdge(
				this,
				identifier,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class
			);
	}
	
	/**
	 * Gets the node that represents the identifier of the field that is accessed.
	 * @return
	 */
	public GraphNodeIdentifier getIdentifier() {
		return (GraphNodeIdentifier)this.identifierEdge.getDestinationNode();
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return
			new GraphNodeFieldAccessSuper(
				graph,
				(GraphNodeIdentifier)this.identifierEdge.getDestinationNode().clone(graph)
			);
	}
	
	@Override
	public String getLabel() {
		return "Super Field Access";
	}
	
	@Override
	public String toString() {
		return "super." + this.getIdentifier().toString();
	}
}