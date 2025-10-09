package nl.ou.refactoring.advice.nodes.code;

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
				(source, destination) -> new GraphEdgeHas(source, destination),
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
}
