package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;

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

	@Override
	public String getLabel() {
		return "Interface";
	}

	@Override
	public String getCaption() {
		return this.interfaceName;
	}
}
