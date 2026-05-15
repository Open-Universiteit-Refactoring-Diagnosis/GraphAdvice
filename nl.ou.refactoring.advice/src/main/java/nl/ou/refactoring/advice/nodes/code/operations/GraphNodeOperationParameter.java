package nl.ou.refactoring.advice.nodes.code.operations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeIs;
import nl.ou.refactoring.advice.edges.code.GraphEdgeList;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;

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
	 * Gets the node that represents the next Operation Parameter.
	 * @return The node that represents the next Operation Parameter, or null if there is none.
	 */
	public GraphNodeOperationParameter getNext() {
		return
			this
				.getEdges(GraphEdgeList.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeOperationParameter)
				.map(node -> (GraphNodeOperationParameter)node)
				.findAny()
				.orElse(null);
	}
	
	/**
	 * Gets the name of the parameter.
	 * @return The name of the parameter.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
	
	/**
	 * Gets the type of the parameter.
	 * @return The type of the parameter.
	 */
	public GraphNodeType getParameterType() {
		return
			this
				.getEdges(GraphEdgeIs.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeType)
				.map(GraphNodeType.class::cast)
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Indicates that the Operation Parameter is of a particular data Type.
	 * @param typeNode The node that describes the data Type.
	 * @return The edge that connects the Operation Parameter and the data Type.
	 * @throws ArgumentNullException Thrown if typeNode is null.
	 */
	public GraphEdgeIs is(GraphNodeType typeNode)
			throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				typeNode,
				(sourceNode, destinationNode) -> new GraphEdgeIs(sourceNode, destinationNode),
				GraphEdgeIs.class);
	}
	
	/**
	 * Indicates that the node that represents an Operation Parameter is the next node in a list.
	 * @param operationParameterNode The node that represents a next Operation Parameter.
	 * @return An edge that indicates that the next node that represents an Operation Parameter follows this node in an ordered list.
	 */
	public GraphEdgeList hasNext(GraphNodeOperationParameter operationParameterNode) {
		ArgumentGuard.requireNotNull(operationParameterNode, "operationParameterNode");
		return this.graph.computeEdge(
			this,
			operationParameterNode,
			(source, destination) -> new GraphEdgeList(source, destination),
			GraphEdgeList.class
		);
	}
	
	/**
	 * Gets the signature of the operation parameter.
	 * @return The signature of the operation parameter.
	 */
	public GraphNodeOperationParameterSignature getSignature() {
		return new GraphNodeOperationParameterSignature(this.getParameterName(), this.getParameterType().getTypeName());
	}
	
	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeOperationParameter(graph, this.parameterName);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		
		if (!(other instanceof GraphNodeOperationParameter)) {
			return false;
		}
		
		final var parameterOther = (GraphNodeOperationParameter)other;
		final var parameterTypeThis = this.getParameterType();
		final var parameterTypeOther = parameterOther.getParameterType();
		return
			this.getParameterName().equals(parameterOther.getParameterName()) &&
			(
				(parameterTypeThis == null && parameterTypeOther == null) ||
				(parameterTypeThis != null && parameterTypeThis.equals(parameterTypeOther))
			);
	}

	@Override
	public String getLabel() {
		return "Operation parameter";
	}

	@Override
	public String getCaption() {
		return this.parameterName;
	}
}
