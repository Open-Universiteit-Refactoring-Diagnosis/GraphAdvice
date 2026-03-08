package nl.ou.refactoring.advice.nodes.code.classes;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;

/**
 * An abstract representation of a class member such as an attribute or an operation.
 */
public abstract class GraphNodeClassMember extends GraphNodeCode {
	/**
	 * Initialises a new instance of {@link GraphNodeClassMember}.
	 * @param graph The graph that owns this node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeClassMember(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Gets the class node that owns this member.
	 * @return The class node that owns this member.
	 */
	public Optional<GraphNodeClass> getClassNode() {
		return
			this
				.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> GraphNodeClass.class.isAssignableFrom(node.getClass()))
				.map(GraphNodeClass.class::cast)
				.findFirst();
	}
}