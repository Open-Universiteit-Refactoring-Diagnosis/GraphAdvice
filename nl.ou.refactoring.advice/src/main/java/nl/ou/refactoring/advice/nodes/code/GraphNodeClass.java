package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;

/**
 * Represents a node in a Refactoring Advice Graph that describes an affected Class.
 */
public final class GraphNodeClass extends GraphNodeCode {
	private final String className;
	
	/**
	 * Initialises a new instance of {@link GraphNodeClass}.
	 * @param graph {@link Graph}  The graph that contains the node.
	 * @param className The name of the affected Class.
	 * @throws ArgumentNullException Thrown if graph or className is null.
	 * @throws ArgumentEmptyException Thrown if className is empty or contains only white spaces.
	 */
	public GraphNodeClass(Graph graph, String className)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(className, "className");
		this.className = className;
	}
	
	/**
	 * Gets the name of the affected Class.
	 * @return The name of the affected Class.
	 */
	public String getClassName() {
		return this.className;
	}
	
	/**
	 * Indicates that the Class owns an Attribute.
	 * @param attributeNode The Attribute that is owned by the Class.
	 * @return The edge that connects the Class and the Attribute.
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
	 * Indicates that the Class owns an Operation.
	 * @param operationNode The Operation that is owned by the Class.
	 * @return The edge that connects the Class and the Operation.
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
	 * Indicates that the Class is a specialisation of a generalised Class.
	 * @param generalisedClassNode The generalised Class.
	 * @return The edge that connects the generalised and specialised Classes.
	 * @throws ArgumentNullException Thrown if generalisedClassNode is null.
	 */
	public GraphEdgeIs is(GraphNodeClass generalisedClassNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				generalisedClassNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}
	
	/**
	 * Indicates that the Class implements an Interface.
	 * @param interfaceNode The Interface that is implemented by the current Class.
	 * @return The edge that connects the Class and the implemented Interface.
	 * @throws ArgumentNullException Thrown if interfaceNode is null.
	 */
	public GraphEdgeIs is(GraphNodeInterface interfaceNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				interfaceNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}

	@Override
	public String getLabel() {
		return "Class";
	}

	@Override
	public String getCaption() {
		return this.className;
	}
}
