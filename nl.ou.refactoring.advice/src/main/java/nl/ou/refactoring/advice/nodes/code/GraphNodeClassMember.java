package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;

/**
 * An abstract representation of a class member such as an attribute or an operation.
 */
public abstract class GraphNodeClassMember extends GraphNodeCode {
	/**
	 * Initialises a new instance of {@link GraphNodeClassMember}.
	 * @param graph The graph that owns this node.
	 * @throws NullPointerException Thrown if graph is null.
	 */
	protected GraphNodeClassMember(Graph graph)
			throws NullPointerException {
		super(graph);
	}
	
	/**
	 * Gets the class node that owns this member.
	 * @return The class node that owns this member.
	 */
	public GraphNodeClass getClassNode() {
		return
				this
					.getEdgesIncoming(GraphEdgeHas.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.filter(node -> GraphNodeClass.class.isAssignableFrom(node.getClass()))
					.map(GraphNodeClass.class::cast)
					.findFirst()
					.orElse(null);
	}
}