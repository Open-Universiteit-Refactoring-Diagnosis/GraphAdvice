package nl.ou.refactoring.advice.nodes.code;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.SortOrder;

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
	private final GraphNodeClassStereotype stereotype;
	
	/**
	 * Initialises a new instance of {@link GraphNodeClass}.
	 * @param graph {@link Graph}  The graph that contains the node.
	 * @param className The name of the affected Class.
	 * @param stereotype The stereotype of the affected Class.
	 * @throws ArgumentNullException Thrown if graph or className is null.
	 * @throws ArgumentEmptyException Thrown if className is empty or contains only white spaces.
	 */
	public GraphNodeClass(
			Graph graph,
			String className,
			GraphNodeClassStereotype stereotype
	)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(className, "className");
		this.className = className;
		this.stereotype = stereotype;
	}
	
	/**
	 * Initialises a new instance of {@link GraphNodeClass}.
	 * @param graph {@link Graph}  The graph that contains the node.
	 * @param className The name of the affected Class.
	 * @throws ArgumentNullException Thrown if graph or className is null.
	 * @throws ArgumentEmptyException Thrown if className is empty or contains only white spaces.
	 */
	public GraphNodeClass(
			Graph graph,
			String className
	)
			throws ArgumentNullException, ArgumentEmptyException {
		this(graph, className, null);
	}
	
	/**
	 * Gets the name of the affected Class.
	 * @return The name of the affected Class.
	 */
	public String getClassName() {
		return this.className;
	}
	
	/**
	 * Gets the stereotype of the affected Class.
	 * @return The stereotype of the affected Class.
	 */
	public GraphNodeClassStereotype getStereotype() {
		return this.stereotype;
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
	
	/**
	 * Gets all attribute nodes that are included in the class.
	 * @return A set of attribute nodes. The set is not modifiable.
	 */
	public Set<GraphNodeAttribute> getAttributeNodes() {
		return
				this
					.getEdges(GraphEdgeHas.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(node -> GraphNodeAttribute.class.isAssignableFrom(node.getClass()))
					.map(GraphNodeAttribute.class::cast)
					.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets all attribute nodes that are included in the class, sorted by the specified sort order.
	 * @param sortOrder The order in which the attributes should be sorted.
	 * @return A sorted list of attribute nodes. The list is not modifiable.
	 */
	public List<GraphNodeAttribute> getAttributeNodes(SortOrder sortOrder) {
		return
				this
					.getAttributeNodes()
					.stream()
					.sorted(
							(a1, a2) -> {
								return switch (sortOrder) {
									case SortOrder.ASCENDING ->
										a1.getCaption().compareTo(a2.getCaption());
									case SortOrder.DESCENDING ->
										a2.getCaption().compareTo(a1.getCaption());
									default -> 0;
								};
							}
					)
					.collect(Collectors.toUnmodifiableList());
	}
	
	/**
	 * Gets the node of the class that generalises this class.
	 * @return The node of the class that generalises this class.
	 * @throws GraphNodeClassHasMultipleGeneralisationsException Thrown if the class node has multiple generalisation class nodes associated with it.
	 */
	public GraphNodeClass getGeneralisationClassNode()
			throws GraphNodeClassHasMultipleGeneralisationsException {
		final var generalisingNodes =
				this
					.getEdges(GraphEdgeIs.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(node -> GraphNodeClass.class.isAssignableFrom(node.getClass()))
					.map(GraphNodeClass.class::cast)
					.collect(Collectors.toUnmodifiableSet());
		return switch (generalisingNodes.size()) {
			case 0 -> null;
			case 1 -> generalisingNodes.stream().findFirst().get();
			default -> throw new GraphNodeClassHasMultipleGeneralisationsException(this);
		};
	}
	
	/**
	 * Gets all operation nodes that are included in the class.
	 * @return A set of operation nodes. The set is not modifiable.
	 */
	public Set<GraphNodeOperation> getOperationNodes() {
		return
				this
					.getEdges(GraphEdgeHas.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(node -> GraphNodeOperation.class.isAssignableFrom(node.getClass()))
					.map(GraphNodeOperation.class::cast)
					.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets all operation nodes that are included in the class, sorted by the specified sort order.
	 * @param sortOrder The order in which the operations should be sorted.
	 * @return A sorted list of operation nodes. The list is not modifiable.
	 */
	public List<GraphNodeOperation> getOperationNodes(SortOrder sortOrder) {
		return
				this
					.getOperationNodes()
					.stream()
					.sorted(
							(o1, o2) -> {
								return switch (sortOrder) {
									case SortOrder.ASCENDING ->
										o1.getCaption().compareTo(o2.getCaption());
									case SortOrder.DESCENDING ->
										o2.getCaption().compareTo(o1.getCaption());
									default -> 0;
								};
							}
					)
					.collect(Collectors.toUnmodifiableList());
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
