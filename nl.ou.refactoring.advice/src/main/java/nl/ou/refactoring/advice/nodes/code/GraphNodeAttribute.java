package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;

/**
 * Represents a node in a Refactoring Advice Graph that describes an Attribute of a Class that is affected by a refactoring.
 */
public final class GraphNodeAttribute extends GraphNodeClassMember {
	private final String attributeName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeAttribute}.
	 * @param graph The graph that contains the node.
	 * @param attributeName The name of the affected Attribute.
	 * @throws ArgumentNullException Thrown if graph or attributeName is null.
	 * @throws ArgumentEmptyException Thrown if attributeName is empty or contains only white spaces.
	 */
	public GraphNodeAttribute(Graph graph, String attributeName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(attributeName, "attributeName");
		this.attributeName = attributeName;
	}
	
	/**
	 * Gets the name of the Attribute affected by a refactoring.
	 * @return The name of the Attribute affected by a refactoring.
	 */
	public String getAttributeName() {
		return this.attributeName;
	}
	
	/**
	 * Gets the Type of the Attribute, if defined. If not, returns null.
	 * @return The Type of the Attribute, if defined. If not, returns null.
	 */
	public GraphNodeType getType() {
		final var edgeIs =
				this
					.getEdges()
					.stream()
					.filter(edge -> GraphEdgeIs.class.isAssignableFrom(edge.getClass()))
					.findFirst()
					.orElse(null);
		if (edgeIs == null) {
			return null;
		}
		
		return (GraphNodeType)edgeIs.getDestinationNode();
	}
	
	/**
	 * Indicates that the Attribute is of the specified Type.
	 * @param typeNode The node that represents the data type.
	 * @return The edge that connects the Attribute and the Type.
	 * @throws ArgumentNullException Thrown if typeNode is null.
	 */
	public GraphEdgeIs is(GraphNodeType typeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				typeNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}

	@Override
	public String getLabel() {
		return "Attribute";
	}

	@Override
	public String getCaption() {
		return this.attributeName;
	}
}
