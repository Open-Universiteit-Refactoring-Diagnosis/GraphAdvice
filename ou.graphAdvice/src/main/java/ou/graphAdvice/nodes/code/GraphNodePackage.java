package ou.graphAdvice.nodes.code;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentEmptyException;
import ou.graphAdvice.contracts.ArgumentGuard;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.code.GraphEdgeHas;

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
		return this.graph.addEdge(
				this,
				classNode,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class);
	}
}
