package nl.ou.refactoring.advice.nodes.code.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassMember;
import nl.ou.refactoring.advice.nodes.code.operations.expressions.GraphNodeMethodInvocationExpression;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

/**
 * Represents a node in a Refactoring Advice Graph that describes an Operation of a Class that is affected by a refactoring.
 */
public final class GraphNodeOperation extends GraphNodeClassMember {
	private final GraphEdgeHas operationNameEdge;
	private final GraphEdgeHas operationParameterHead;
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperation}.
	 * @param graph The graph that contains the node.
	 * @param operationName The node that represents the name of the operation.
	 * @param operationParameters The parameters of the operation. If null, an empty list will be created.
	 * @throws ArgumentNullException Thrown if graph, operationName or operationParameters are null.
	 * @throws ArgumentEmptyException Thrown if operationName is empty or contains only white spaces.
	 */
	public GraphNodeOperation(
		Graph graph,
		GraphNodeIdentifier operationName,
		List<GraphNodeOperationParameter> operationParameters
	) throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(operationName, "operationName");
		ArgumentGuard.requireNotNull(operationParameters, "operationParameters");
		super(graph);
		this.operationNameEdge =
			this.graph.getOrAddEdge(
				this,
				operationName,
				(source, destination) -> new GraphEdgeHas(source, destination),
				GraphEdgeHas.class
			);
		
		if (operationParameters.size() == 0) {
			this.operationParameterHead = null;
		} else {
			var node = operationParameters.get(0);
			if (node == null) {
				throw new GraphNodeOperationParameterNullReferenceException(this, 0);
			}
			this.operationParameterHead =
				this.graph.getOrAddEdge(
					this,
					operationParameters.get(0),
					(source, destination) -> new GraphEdgeHas(source, destination),
					GraphEdgeHas.class
				);
			GraphNodeOperationParameter nodeNext;
			for (var i = 1; i < operationParameters.size(); i++) {
				nodeNext = operationParameters.get(i);
				if (nodeNext == null) {
					throw new GraphNodeOperationParameterNullReferenceException(this, i);
				}
				node.hasNext(nodeNext);
				node = nodeNext;
			}
		}
	}
	
	/**
	 * Initialises a new instance of {@link GraphNodeOperation}.
	 * @param graph The graph that contains the node.
	 * @param operationName The node that represents the name of the operation.
	 * @throws ArgumentNullException Thrown if graph or operationName are null.
	 * @throws ArgumentEmptyException Thrown if operationName is empty or contains only white spaces.
	 */
	public GraphNodeOperation(Graph graph, GraphNodeIdentifier operationName)
			throws ArgumentNullException, ArgumentEmptyException {
		this(graph, operationName, new ArrayList<GraphNodeOperationParameter>());
	}
	
	/**
	 * Gets the return Type of the Operation, if defined. If not, returns null.
	 * @return The return Type of the Operation, if defined. If not, returns null.
	 */
	public Optional<GraphNodeType> getReturnType() {
		final var edgeIs =
			this
				.getEdges()
				.stream()
				.filter(edge -> GraphEdgeIs.class.isAssignableFrom(edge.getClass()))
				.findFirst();
		if (edgeIs.isPresent()) {
			return Optional.of((GraphNodeType)edgeIs.get().getDestinationNode());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * Gets the name of the Operation affected by a refactoring.
	 * @return The name of the Operation affected by a refactoring.
	 */
	public String getOperationName() {
		return this.operationNameEdge.getDestinationNode().toString();
	}
	
	/**
	 * Gets the {@link GraphNodeMicrostepAddMethod} microstep node that added this {@link GraphNodeOperation}.
	 * @return The {@link GraphNodeMicrostepAddMethod} microstep node that added this {@link GraphNodeOperation}, if any, otherwise empty.
	 */
	public Optional<GraphNodeMicrostepAddMethod> getAddedBy() {
		return
			this
				.getEdgesIncoming(GraphEdgeAdds.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeMicrostepAddMethod.class::isInstance)
				.map(GraphNodeMicrostepAddMethod.class::cast)
				.findFirst();
	}
	
	/**
	 * Gets the {@link GraphNodeMicrostepRemoveMethod} microstep node that removed this {@link GraphNodeOperation}.
	 * @return The {@link GraphNodeMicrostepRemoveMethod} microstep node that removed this {@link GraphNodeOperation}, if any, otherwise empty.
	 */
	public Optional<GraphNodeMicrostepRemoveMethod> getRemovedBy() {
		return
			this
				.getEdgesIncoming(GraphEdgeRemoves.class)
				.stream()
				.map(edge -> edge.getSourceNode())
				.filter(GraphNodeMicrostepRemoveMethod.class::isInstance)
				.map(GraphNodeMicrostepRemoveMethod.class::cast)
				.findFirst();
	}
	
	/**
	 * Gets the Operation Parameters of this Operation.<br />
	 * <strong>Note:</strong> The returned {@link List List&lt;GraphNodeOperationParameter&gt;} is not modifiable.
	 * @return The Operation Parameters of this Operation.
	 */
	public List<GraphNodeOperationParameter> getOperationParameters() {
		if (this.operationParameterHead == null) {
			return List.of();
		}
		final var operationParameterList = new ArrayList<GraphNodeOperationParameter>();
		var node = (GraphNodeOperationParameter)this.operationParameterHead.getDestinationNode();
		while (node != null) {
			operationParameterList.add(node);
			node = node.getNext();
		}
		return operationParameterList;
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
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		final var operationNameClone = (GraphNodeIdentifier)this.operationNameEdge.getDestinationNode().clone(graph);
		final var operationParameterClones =
			this
				.getOperationParameters()
				.stream()
				.map(node -> (GraphNodeOperationParameter)node.clone(graph))
				.collect(Collectors.toUnmodifiableList());
		return new GraphNodeOperation(
			graph,
			operationNameClone,
			operationParameterClones
		);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (!(other instanceof GraphNodeOperation)) {
			return false;
		}
		
		// Check references are equal.
		if (this == other) {
			return true;
		}
		
		// Check name and parameters.
		final var operationNode = (GraphNodeOperation)other;
		if (!this.getOperationName().equals(operationNode.getOperationName()) ||
			!this.getOperationParameters().equals(operationNode.getOperationParameters())) {
			return false;
		}
		
		// Check Class.
		final var classNodeThis = this.getClassNode();
		final var classNodeOther = operationNode.getClassNode();
		if (classNodeThis == null || classNodeOther == null ||
			!classNodeThis.getClassName().equals(classNodeOther.getClassName())) {
			return false;
		}
		
		return true;
	}

	@Override
	public String getLabel() {
		return "Operation";
	}

	@Override
	public String getCaption() {
		return this.getOperationName();
	}
}
