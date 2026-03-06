package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * Represents a node in a Refactoring Advice Graph that describes an affected Interface.
 */
public final class GraphNodeInterface extends GraphNodeCode {
	private final String interfaceName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeInterface}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @param interfaceName The name of the affected Interface.
	 * @throws ArgumentNullException Thrown if graph or interfaceName is null.
	 * @throws ArgumentEmptyException Thrown if interfaceName is empty or contains only white spaces.
	 */
	public GraphNodeInterface(Graph graph, String interfaceName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(interfaceName, "interfaceName");
		this.interfaceName = interfaceName;
	}
	
	/**
	 * Gets the name of the interface.
	 * @return The name of the interface.
	 */
	public String getInterfaceName() {
		return this.interfaceName;
	}
	
	/**
	 * Indicates that the Interface owns an Attribute.
	 * @param attributeNode The Attribute that is owned by the Interface.
	 * @return The edge that connects the Interface and the Attribute.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeHas has(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				attributeNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
				
	}
	
	/**
	 * Indicates that the Interface owns an Operation.
	 * @param operationNode The Operation that is owned by the Interface.
	 * @return The edge that connects the Interface and the Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeHas has(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				operationNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
	}
	
	/**
	 * Indicates that the Interface derives from another Interface.
	 * @param baseInterfaceNode The base Interface that this Interface derives from.
	 * @return The edge that connects the derived Interface and the base Interface.
	 * @throws ArgumentNullException Thrown if baseInterfaceNode is null.
	 */
	public GraphEdgeIs is(GraphNodeInterface baseInterfaceNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				baseInterfaceNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}
	
	/**
	 * Gets the interface that is extended by this interface, if any.
	 * @return The interface that is extended by this interface, though null if none apply.
	 */
	public GraphNodeInterface getBaseInterface() {
		return this.getEdges(GraphEdgeIs.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeInterface)
				.map(GraphNodeInterface.class::cast)
				.findAny()
				.orElse(null);
	}
	
	@Override
	public GraphNodeBase clone(Graph graph) {
		return new GraphNodeInterface(graph, this.interfaceName);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (other == null || !(other instanceof GraphNodeInterface)) {
			return false;
		}
		final var interfaceNode = (GraphNodeInterface)other;
		return
			this.getInterfaceName().equals(interfaceNode.getInterfaceName()) &&
			((this.getBaseInterface() == null && interfaceNode.getBaseInterface() == null) ||
			this.getBaseInterface().equals(interfaceNode.getBaseInterface()));
	}

	@Override
	public String getLabel() {
		return "Interface";
	}

	@Override
	public String getCaption() {
		return this.interfaceName;
	}
}