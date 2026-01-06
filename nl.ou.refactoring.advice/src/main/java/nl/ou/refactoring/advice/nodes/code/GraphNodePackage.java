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

/**
 * Represents a node in a Refactoring Advice Graph that represents a programme code package that is affected by a refactoring.
 */
public final class GraphNodePackage extends GraphNodeCode {
	private final String packageName;
	
	/**
	 * Initialises a new instance of {@link GraphNodePackage}.
	 * @param graph The graph that contains the node.
	 * @param packageName The name of the package that is affected by a refactoring.
	 * @throws ArgumentNullException Thrown if graph or packageName is null.
	 * @throws ArgumentEmptyException Thrown if packageName is empty or contains only white spaces.
	 */
	public GraphNodePackage(Graph graph, String packageName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(packageName, "packageName");
		this.packageName = packageName;
	}
	
	/**
	 * Gets the name of the package that is affected by a refactoring.
	 * @return The name of the package that is affected by a refactoring.
	 */
	public String getPackageName() {
		return this.packageName;
	}
	
	/**
	 * Indicates that the Package contains a Class.
	 * @param classNode The node that describes the Class in the Package.
	 * @return The edge that connects the Package and the Class.
	 * @throws ArgumentNullException Thrown if classNode is null.
	 */
	public GraphEdgeHas has(GraphNodeClass classNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				classNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
	}
	
	/**
	 * Indicates that the Package contains an Interface.
	 * @param interfaceNode The node that describes the Interface in the Package.
	 * @return The edge that connects the Package and the Interface.
	 * @throws ArgumentNullException Thrown if interfaceNode is null.
	 */
	public GraphEdgeHas has(GraphNodeInterface interfaceNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				interfaceNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
	}
	
	/**
	 * Gets all class nodes that are included in the package.
	 * @return All class nodes that are included in the package.
	 */
	public Set<GraphNodeClass> getClassNodes() {
		return
				this
					.getEdges(GraphEdgeHas.class)
					.stream()
					.map(edge -> edge.getDestinationNode())
					.filter(node -> GraphNodeClass.class.isAssignableFrom(node.getClass()))
					.map(GraphNodeClass.class::cast)
					.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets all class nodes that are included in the package, sorted by the specified sort order.
	 * @param sortOrder The sort order for the classes.
	 * @return A sorted list of class nodes. The list is not modifiable.
	 */
	public List<GraphNodeClass> getClassNodes(SortOrder sortOrder) {
		return
				this
					.getClassNodes()
					.stream()
					.sorted(
							(c1, c2) -> {
								return switch(sortOrder) {
									case SortOrder.ASCENDING ->
										c1.getCaption().compareTo(c2.getCaption());
									case SortOrder.DESCENDING ->
										c2.getCaption().compareTo(c1.getCaption());
									default -> 0;
								};
							}
					)
					.collect(Collectors.toUnmodifiableList());
					
	}
	
	/**
	 * Gets the {@link GraphNodeClass} in this package with className.
	 * If a class with className is not present, it is added and the newly created class node is returned.
	 * @param className The name of the class to get or add.
	 * @return The existing or newly created {@link GraphNodeClass}.
	 */
	public GraphNodeClass computeClassNode(String className) {
		var classNode =
				this
					.getClassNodes()
					.stream()
					.filter(node -> node.getClassName() == className)
					.findFirst()
					.orElse(null);
		if (classNode == null) {
			classNode = new GraphNodeClass(this.graph, className);
			this.has(classNode);
		}
		return classNode;
	}

	@Override
	public String getLabel() {
		return "Package";
	}

	@Override
	public String getCaption() {
		return this.packageName;
	}
}
