package nl.ou.refactoring.advice.nodes.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;

/**
 * Represents a node in a Refactoring Advice Graph that describes an Operation of a Class that is affected by a refactoring.
 */
public final class GraphNodeOperation extends GraphNodeClassMember {
	private final String operationName;
	private final List<GraphNodeOperationParameter> operationParameters;
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperation}.
	 * @param graph The graph that contains the node.
	 * @param operationName The name of the operation.
	 * @throws ArgumentNullException Thrown if graph or operationName are null.
	 * @throws ArgumentEmptyException Thrown if operationName is empty or contains only white spaces.
	 */
	public GraphNodeOperation(Graph graph, String operationName)
			throws ArgumentNullException, ArgumentEmptyException {
		this(graph, operationName, new ArrayList<GraphNodeOperationParameter>());
	}
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperation}.
	 * @param graph The graph that contains the node.
	 * @param operationName The name of the operation.
	 * @param operationParameters The parameters of the operation. If null, an empty list will be created.
	 * @throws ArgumentNullException Thrown if graph or operationName are null.
	 * @throws ArgumentEmptyException Thrown if operationName is empty or contains only white spaces.
	 */
	public GraphNodeOperation(
			Graph graph,
			String operationName,
			List<GraphNodeOperationParameter> operationParameters
	)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(operationName, "operationName");
		this.operationName = operationName;
		this.operationParameters =
				operationParameters == null
					? new ArrayList<GraphNodeOperationParameter>()
					: operationParameters;
	}
	
	/**
	 * Gets the return Type of the Operation, if defined. If not, returns null.
	 * @return The return Type of the Operation, if defined. If not, returns null.
	 */
	public GraphNodeType getReturnType() {
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
	 * Gets the name of the Operation affected by a refactoring.
	 * @return The name of the Operation affected by a refactoring.
	 */
	public String getOperationName() {
		return this.operationName;
	}
	
	/**
	 * Gets the Operation Parameters of this Operation.<br />
	 * <strong>Note:</strong> The returned {@link List List&lt;GraphNodeOperationParameter&gt;} is not modifiable.
	 * @return The Operation Parameters of this Operation.
	 */
	public List<GraphNodeOperationParameter> getOperationParameters() {
		return Collections.unmodifiableList(this.operationParameters);
	}
	
	/**
	 * Indicates that the Operation has a particular return Type.
	 * @param typeNode The node that describes the return Type.
	 * @return The edge that connects the Operation and the Type.
	 * @throws ArgumentNullException Thrown if typeNode is null.
	 */
	public GraphEdgeIs hasReturnType(GraphNodeType typeNode)
			throws ArgumentNullException {
		return this.graph.getOrAddEdge(
				this,
				typeNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}

	@Override
	public String getLabel() {
		return "Operation";
	}

	@Override
	public String getCaption() {
		return this.operationName;
	}
}
