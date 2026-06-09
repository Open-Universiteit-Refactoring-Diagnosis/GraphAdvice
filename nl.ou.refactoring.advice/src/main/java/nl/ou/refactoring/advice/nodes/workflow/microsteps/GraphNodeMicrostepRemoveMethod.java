package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes a Method.
 */
public final class GraphNodeMicrostepRemoveMethod extends GraphNodeMicrostepRemove {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveMethod}.
	 * @param graph The graph that contains the node. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveMethod(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Remove Method" microstep removes the operation represented by operationNode.
	 * @param operationNode The operation that is removed by this microstep.
	 * @return The edge that indicates that the "Remove Method" microstep removes the operation represented by operationNode.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeRemoves removes(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		return
			this.graph.computeEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeRemoves(source, destination),
				GraphEdgeRemoves.class
			);
	}
	
	/**
	 * Returns the node that represents the method/operation that is being removed by this microstep.
	 * @return The node that represents the method/operation that is being removed by this microstep.
	 * @throws GraphNodeMicrostepOperationMissingException Thrown if no operation node is associated with the microstep.
	 */
	public GraphNodeOperation getOperationNode() throws GraphNodeMicrostepOperationMissingException {
		return
			this
				.getEdges(GraphEdgeRemoves.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(GraphNodeOperation.class::isInstance)
				.map(GraphNodeOperation.class::cast)
				.findAny()
				.orElseThrow(() -> new GraphNodeMicrostepOperationMissingException(this));
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepRemoveMethod(graph);
	}
}