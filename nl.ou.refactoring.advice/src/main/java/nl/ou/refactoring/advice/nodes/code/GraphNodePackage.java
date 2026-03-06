package nl.ou.refactoring.advice.nodes.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.SortOrder;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

/**
 * Represents a node in a Refactoring Advice Graph that represents a programme code package that is affected by a refactoring.
 */
public final class GraphNodePackage extends GraphNodeCode {
	/**
	 * The edge to the package name.
	 */
	private final GraphEdgeHas packageNameEdge;
	
	/**
	 * Initialises a new instance of {@link GraphNodePackage}.
	 * @param graph The graph that contains the node.
	 * @param packageName The name of the package that is affected by a refactoring.
	 * @throws ArgumentNullException Thrown if graph or packageName is null.
	 */
	public GraphNodePackage(Graph graph, GraphNodeIdentifier packageName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNull(packageName, "packageName");
		this.packageNameEdge =
			this
				.graph
				.getOrAddEdge(
					this,
					packageName,
					(source, destination) -> new GraphEdgeHas(source, destination),
					GraphEdgeHas.class
				);
	}
	
	/**
	 * Parses a tree of package nodes from a package's name.
	 * @param graph The graph that contains the packages.
	 * @param packageName The name of the package.
	 * @return The node that represents the package root.
	 * @throws ArgumentNullException Thrown if graph or packageName is null.
	 * @throws ArgumentEmptyException Thrown if packageName is empty or contains only white spaces.
	 */
	public static GraphNodePackage parse(Graph graph, String packageName)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(packageName, "packageName");
		final var pathComponents = new ArrayDeque<String>(List.of(packageName.split("\\.")));
		final var pathComponentRoot = pathComponents.pop();
		final var packageNodeRootOptional = graph.getNode(pathComponentRoot, GraphNodePackage.class);
		GraphNodePackage packageNodeRoot;
		if (packageNodeRootOptional.isPresent()) {
			packageNodeRoot = packageNodeRootOptional.get();
		} else {
			packageNodeRoot = new GraphNodePackage(graph, new GraphNodeIdentifier(graph, pathComponentRoot));
		}
		var packageNode = packageNodeRoot;
		while (pathComponents.size() > 0) {
			final var pathComponent = pathComponents.pop();
			final var packageNodeCurrent = packageNode;
			final var packageNodeNextOptional = packageNodeCurrent.getPackage(pathComponent);
			if (packageNodeNextOptional.isPresent()) {
				packageNode = packageNodeNextOptional.get();
				continue;
			}
			final var packageNameComponent = new GraphNodeIdentifier(graph, pathComponent);
			final var packageNodeComponent = new GraphNodePackage(graph, packageNameComponent);
			packageNodeCurrent.has(packageNodeComponent);
			packageNode = packageNodeComponent;
		}
		return packageNodeRoot;
	}
	
	/**
	 * Gets the name of the package that is affected by a refactoring.
	 * @return The name of the package that is affected by a refactoring.
	 */
	public String getPackageName() {
		return this.packageNameEdge.getDestinationNode().toString();
	}
	
	/**
	 * Gets the full package name, including the ancestor packages.
	 * @return The full package name, including the ancestor packages.
	 */
	public String getPackageNameFull() {
		Optional<GraphNodePackage> packageNodeCurrent = Optional.of(this);
		final var packageNameStack = new ArrayDeque<String>();
		while (packageNodeCurrent.isPresent()) {
			packageNameStack.push(packageNodeCurrent.get().getPackageName());
			packageNodeCurrent = packageNodeCurrent.get().getParent();
		}
		return String.join(".", packageNameStack);
	}
	
	/**
	 * Indicates that the Package contains a child Package.
	 * @param packageNode The node that represents the child package.
	 * @return The edge that indicates that the specified node represents a child package.
	 * @throws ArgumentNullException Thrown if packageNode is null.
	 */
	public GraphEdgeHas has(GraphNodePackage packageNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
			this,
			packageNode,
			(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
			GraphEdgeHas.class
		);
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
			GraphEdgeHas.class
		);
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
			GraphEdgeHas.class
		);
	}
	
	/**
	 * Gets the node that represents the parent package.
	 * @return The node that represents the parent package, or an empty {@link Optional<GraphNodePackage>} if this is a root package.
	 */
	public Optional<GraphNodePackage> getParent() {
		return
			this
				.getEdgesIncoming(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(node -> node instanceof GraphNodePackage)
				.map(GraphNodePackage.class::cast)
				.findAny();
	}
	
	/**
	 * Gets the nodes that represent the child packages.
	 * @return An unmodifiable set of child packages.
	 */
	public Set<GraphNodePackage> getPackageNodes() {
		return
			this
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodePackage)
				.map(GraphNodePackage.class::cast)
				.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets the package nodes in a package tree that do not have any child nodes (the leafs of the tree).
	 * @return An unmodifiable set of package nodes at the extremes of the package tree.
	 */
	public Set<GraphNodePackage> getPackageNodeLeafs() {
		final var packageNodeTreeStack = new Stack<GraphNodePackage>();
		final var packageNodeLeafList = new ArrayList<GraphNodePackage>();
		packageNodeTreeStack.push(this);
		
		while (!packageNodeTreeStack.isEmpty()) {
			final var packageNodeCurrent = packageNodeTreeStack.pop();
			final var packageNodeChildren = packageNodeCurrent.getPackageNodes();
			if (packageNodeChildren.isEmpty()) {
				packageNodeLeafList.add(packageNodeCurrent);
			} else {
				for (final var packageNodeChild : packageNodeChildren) {
					packageNodeTreeStack.push(packageNodeChild);
				}
			}
		}
		
		return Collections.unmodifiableSet(new HashSet<>(packageNodeLeafList));
	}
	
	/**
	 * Gets the associated node that represents the child package with the specified package name.
	 * If not found, returns an empty {@link Optional<GraphNodePackage>}.
	 * @param packageName The name of the requested child package.
	 * @return The node that represents the requested child package wrapped in {@link Optional<GraphNodePackage>}, otherwise an empty {@link Optional<GraphNodePackage>}.
	 */
	public Optional<GraphNodePackage> getPackage(String packageName) {
		return
			this
				.getPackageNodes()
				.stream()
				.filter(
					packageNode ->
					packageNode.getPackageName().equals(packageName)
				)
				.findAny();
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
	public GraphNodeClass computeClassNode(GraphNodeIdentifier className) {
		var classNode =
			this
				.getClassNodes()
				.stream()
				.filter(node -> node.getClassName().equals(className.getIdentifier()))
				.findFirst()
				.orElse(null);
		if (classNode == null) {
			classNode = new GraphNodeClass(this.graph, className);
			this.has(classNode);
		}
		return classNode;
	}
	
	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodePackage(
			graph,
			(GraphNodeIdentifier)this.packageNameEdge.getDestinationNode().clone(graph)
		);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		return
			other != null &&
			other instanceof GraphNodePackage &&
			this.getPackageName().equals(((GraphNodePackage)other).getPackageName());
	}

	@Override
	public String getLabel() {
		return "Package";
	}

	@Override
	public String getCaption() {
		return this.getPackageName();
	}
	
	@Override
	public String toString() {
		return this.getCaption();
	}
}