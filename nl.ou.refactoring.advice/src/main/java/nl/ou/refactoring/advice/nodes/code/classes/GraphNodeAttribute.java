package nl.ou.refactoring.advice.nodes.code.classes;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;
import nl.ou.refactoring.advice.edges.mechanics.GraphEdgeAdds;
import nl.ou.refactoring.advice.edges.mechanics.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCodeAccessModifier;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostepAddField;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostepRemoveField;

/**
 * Represents a node in a Refactoring Advice Graph that describes an Attribute
 * of a Class that is affected by a refactoring.
 */
public final class GraphNodeAttribute extends GraphNodeClassMember {
	private final GraphEdgeHas attributeNameEdge;

	/**
	 * Initialises a new instance of {@link GraphNodeAttribute}.
	 * 
	 * @param graph          The graph that contains the node.
	 * @param attributeName  The name of the Attribute.
	 * @param accessModifier The access modifier of the Attribute.
	 * @throws ArgumentNullException Thrown if graph, attributeName or
	 *                               accessModifier is null.
	 */
	public GraphNodeAttribute(
			Graph graph,
			GraphNodeIdentifier attributeName,
			GraphNodeCodeAccessModifier accessModifier) throws ArgumentNullException {
		super(graph, accessModifier);
		ArgumentGuard.requireNotNull(attributeName, "attributeName");
		ArgumentGuard.requireNotNull(accessModifier, "accessModifier");
		this.attributeNameEdge =
				this.graph.computeEdge(
						this,
						attributeName,
						(source, destination) -> new GraphEdgeHas(source, destination),
						GraphEdgeHas.class);
	}

	/**
	 * Initialises a new instance of {@link GraphNodeAttribute}.
	 * 
	 * @param graph         The graph that contains the node.
	 * @param attributeName The name of the Attribute.
	 * @throws ArgumentNullException Thrown if graph or attributeName is null.
	 */
	public GraphNodeAttribute(Graph graph, GraphNodeIdentifier attributeName) throws ArgumentNullException {
		this(graph, attributeName, GraphNodeCodeAccessModifier.PACKAGE);
	}

	/**
	 * Gets the name of the Attribute affected by a refactoring.
	 * 
	 * @return The name of the Attribute affected by a refactoring.
	 */
	public String getAttributeName() {
		return ((GraphNodeIdentifier) this.attributeNameEdge.getDestinationNode()).getIdentifier();
	}

	/**
	 * Gets the Type of the Attribute wrapped in {@link Optional}, if defined. If
	 * not, returns an empty {@link Optional}.
	 * 
	 * @return The Type of the Attribute wrapped in {@link Optional}, if defined. If
	 *         not, returns an empty {@link Optional}.
	 */
	public Optional<GraphNodeType> getType() {
		return this.getEdges()
				.stream()
				.filter(edge -> GraphEdgeIs.class.isAssignableFrom(edge.getClass()))
				.map(edge -> (GraphNodeType) edge.getDestinationNode())
				.findFirst();
	}

	/**
	 * Gets the {@link GraphNodeMicrostepAddField} microstep node that added this
	 * {@link GraphNodeAttribute}.
	 * 
	 * @return The {@link GraphNodeMicrostepAddField} microstep node that added this
	 *         {@link GraphNodeAttribute}, if any, otherwise empty.
	 */
	public Optional<GraphNodeMicrostepAddField> getAddedBy() {
		return this.getEdgesIncoming(GraphEdgeAdds.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeMicrostepAddField.class::isInstance)
				.map(GraphNodeMicrostepAddField.class::cast)
				.findFirst();
	}

	/**
	 * Gets the {@link GraphNodeMicrostepRemoveField} microstep node that removed
	 * this {@link GraphNodeAttribute}.
	 * 
	 * @return The {@link GraphNodeMicrostepRemoveField} microstep node that removed
	 *         this {@link GraphNodeAttribute}, if any, otherwise empty.
	 */
	public Optional<GraphNodeMicrostepRemoveField> getRemovedBy() {
		return this.getEdgesIncoming(GraphEdgeRemoves.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeMicrostepRemoveField.class::isInstance)
				.map(GraphNodeMicrostepRemoveField.class::cast)
				.findFirst();
	}

	/**
	 * Indicates that the Attribute is of the specified Type.
	 * 
	 * @param typeNode The node that represents the data type.
	 * @return The edge that connects the Attribute and the Type.
	 * @throws ArgumentNullException Thrown if typeNode is null.
	 */
	public GraphEdgeIs is(GraphNodeType typeNode) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				typeNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}

	@Override
	public GraphNodeBase clone(Graph graph) {
		return new GraphNodeAttribute(
				graph,
				(GraphNodeIdentifier) this.attributeNameEdge.getDestinationNode().clone(graph));
	}

	@Override
	public boolean equals(GraphNode other) {
		if (other == null || !(other instanceof GraphNodeAttribute)) {
			return false;
		}
		final var attributeNode = (GraphNodeAttribute) other;
		return this.getAttributeName().equals(attributeNode.getAttributeName())
				&& ((this.getType() == null && attributeNode.getType() == null)
						|| this.getType().equals(attributeNode.getType()));
	}

	@Override
	public String getLabel() {
		return "Attribute";
	}

	@Override
	public String getCaption() {
		return ((GraphNodeIdentifier) this.attributeNameEdge.getDestinationNode()).getIdentifier();
	}

	@Override
	public String toString() {
		final var stringBuilder = new StringBuilder();

		// Attribute
		final var attributeName = this.getAttributeName();
		final var attributeAccessModifier = this.getAccessModifier().toString();
		stringBuilder.append("Attribute node: " + attributeName + " (" + attributeAccessModifier + ")\n");

		// Declaring class
		final var classNode = this.getClassNode();
		classNode.ifPresent((cn) -> {
			stringBuilder.append("Declaring class: " + cn.getClassName());
		});

		return stringBuilder.toString();
	}
}
