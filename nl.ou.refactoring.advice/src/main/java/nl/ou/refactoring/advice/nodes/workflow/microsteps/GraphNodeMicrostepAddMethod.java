package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Method.
 */
public final class GraphNodeMicrostepAddMethod extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddMethod}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddMethod(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Add Method" microstep adds the operation represented by operationNode.
	 * @param operationNode The operation that is added by this microstep.
	 * @return The edge that indicates that the "Add Method" microstep adds the operation represented by operationNode.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(operationNode, "operationNode");
		return
			this
				.graph
				.computeEdge(
					this,
					operationNode,
					(source, destination) -> new GraphEdgeAdds(source, destination),
					GraphEdgeAdds.class
				);
	}
	
	/**
	 * Gets the {@link GraphNodeOperation} node that represents the operation that is added by the microstep.
	 * @return The {@link GraphNodeOperation} node that represents the operation that is added by the microstep, wrapped in an {@link Optional<GraphNodeOperation>}, or an empty {@link Optional<GraphNodeOperation>} if there is none.
	 */
	public Optional<GraphNodeOperation> getOperationNode() {
		return
			this
				.getEdges(GraphEdgeAdds.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeOperation)
				.map(GraphNodeOperation.class::cast)
				.findAny();
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepAddMethod(graph);
	}
}
