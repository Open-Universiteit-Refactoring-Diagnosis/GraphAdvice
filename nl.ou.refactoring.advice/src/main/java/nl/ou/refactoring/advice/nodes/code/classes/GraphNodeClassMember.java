package nl.ou.refactoring.advice.nodes.code.classes;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCodeAccessModifier;

/**
 * An abstract representation of a class member such as an attribute or an
 * operation.
 */
public abstract class GraphNodeClassMember extends GraphNodeCode {
	private final GraphNodeCodeAccessModifier accessModifier;

	/**
	 * Initialises a new instance of {@link GraphNodeClassMember}.
	 * 
	 * @param graph          The graph that owns this node.
	 * @param accessModifier The access modifier of the class member.
	 * @throws ArgumentNullException Thrown if graph or accessModifier is null.
	 */
	protected GraphNodeClassMember(Graph graph, GraphNodeCodeAccessModifier accessModifier)
			throws ArgumentNullException {
		super(graph);
		ArgumentGuard.requireNotNull(accessModifier, "accessModifier");
		this.accessModifier = accessModifier;
	}

	/**
	 * Initialises a new instance of {@link GraphNodeClassMember}.
	 * 
	 * @param graph The graph that owns this node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeClassMember(Graph graph) throws ArgumentNullException {
		this(graph, GraphNodeCodeAccessModifier.PACKAGE);
	}

	/**
	 * Gets the class node that owns this member.
	 * 
	 * @return The class node that owns this member.
	 */
	public Optional<GraphNodeClass> getClassNode() {
		return this.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeClass.class::isInstance)
				.map(GraphNodeClass.class::cast)
				.findFirst();
	}

	/**
	 * Gets the access modifier of the class member.
	 * 
	 * @return
	 */
	public final GraphNodeCodeAccessModifier getAccessModifier() {
		return this.accessModifier;
	}
}