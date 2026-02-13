package nl.ou.refactoring.advice.nodes.code.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeInvokes;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassMember;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;

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
	 * @param operationParameters The parameters of the operation. If null, an empty list will be created.
	 * @throws ArgumentNullException Thrown if graph or operationName are null.
	 * @throws ArgumentEmptyException Thrown if operationName is empty or contains only white spaces.
	 */
	public GraphNodeOperation(
			Graph graph,
			String operationName,
			List<GraphNodeOperationParameter> operationParameters
	) throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(operationName, "operationName");
		this.operationName = operationName;
		this.operationParameters =
				operationParameters == null
					? new ArrayList<GraphNodeOperationParameter>()
					: operationParameters;
	}
	
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
	 * Gets the {@link GraphNodeMicrostepAddMethod} microstep node that added this {@link GraphNodeOperation}.
	 * @return The {@link GraphNodeMicrostepAddMethod} microstep node that added this {@link GraphNodeOperation}, if any, otherwise null.
	 */
	public GraphNodeMicrostepAddMethod getAddedBy() {
		return
				this
					.getEdgesIncoming(GraphEdgeAdds.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.map(GraphNodeMicrostepAddMethod.class::cast)
					.findFirst()
					.orElse(null);
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
		ArgumentGuard.requireNotNull(typeNode, "typeNode");
		return this.graph.getOrAddEdge(
				this,
				typeNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}
	
	/**
	 * Indicates that the Operation has a body block represented by blockNode.
	 * @param blockNode The node that represents the code block that serves as the operation's body block.
	 * @return The edge that connects the Operation and its body block.
	 * @throws ArgumentNullException Thrown if blockNode is null.
	 */
	public GraphEdgeHas hasBody(GraphNodeBlock blockNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(blockNode, "blockNode");
		return this.graph.getOrAddEdge(
				this,
				blockNode,
				(sourceNode, destinationNode) -> new GraphEdgeHas(sourceNode, destinationNode),
				GraphEdgeHas.class);
	}
	
	/**
	 * Gets the operation body, if any.
	 * @return A node that represents a code block that serves as the operation's body.
	 */
	public GraphNodeBlock getBody() {
		return
			this
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeBlock)
				.map(GraphNodeBlock.class::cast)
				.findAny()
				.orElse(null);
	}
	
	/**
	 * Gets the {@link GraphNodeMethodInvocationExpression} nodes that represent the method invocation expressions that call this operation.
	 * @return An unmodifiable set of {@link GraphNodeMethodInvocationExpression} nodes that represent the method invocation expressions that call this operation.
	 */
	public Set<GraphNodeMethodInvocationExpression> getInvocations() {
		return
			this
				.getEdgesIncoming(GraphEdgeInvokes.class)
				.stream()
				.map(edge -> edge.getMethodInvocationExpression())
				.collect(Collectors.toUnmodifiableSet());
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
