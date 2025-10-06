package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;

/**
 * Represents a parameter in an operation affected by a refactoring.
 */
public final class GraphNodeOperationParameter extends GraphNodeCode {
	private final String parameterName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperationParameter}.
	 * @param graph The graph that contains the node.
	 * @param parameterName The name of the parameter.
	 * @throws ArgumentNullException Thrown if graph or parameterName is null.
	 * @throws ArgumentEmptyException Thrown if parameterName is empty or contains only white spaces.
	 */
	public GraphNodeOperationParameter(Graph graph, String parameterName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(parameterName, "parameterName");
		this.parameterName = parameterName;
	}
	
	/**
	 * Gets the name of the parameter.
	 * @return The name of the parameter.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
	
	/**
	 * Indicates that the Operation Parameter is of a particular data Type.
	 * @param typeNode The node that describes the data Type.
	 * @return The edge that connects the Operation Parameter and the data Type.
	 * @throws ArgumentNullException Thrown if typeNode is null.
	 */
	public GraphEdgeIs is(GraphNodeType typeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				typeNode,
				(source, destination) -> new GraphEdgeIs(source, destination),
				GraphEdgeIs.class);
	}
}
