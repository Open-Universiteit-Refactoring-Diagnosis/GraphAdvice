package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeRemoves;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes a Field.
 */
public final class GraphNodeMicrostepRemoveField extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveField}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveField(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Remove Field" microstep removes the field represented by attributeNode.
	 * @param attributeNode The attribute that is removed by this microstep.
	 * @return The edge that indicates that the "Remove Field" microstep removes the field represented by attributeNode.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeRemoves removes(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(attributeNode, "attributeNode");
		return
			this
				.graph
				.computeEdge
				(
					this,
					attributeNode,
					(source, destination) -> new GraphEdgeRemoves(source, destination),
					GraphEdgeRemoves.class
				);
	}
	
	/**
	 * Gets the node that represents the attribute/field that is being removed.
	 * @return The node that represents the attribute/field that is being removed.
	 */
	public Optional<GraphNodeAttribute> getAttributeNode() {
		return
			this
				.getEdges(GraphEdgeRemoves.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(GraphNodeAttribute.class::isInstance)
				.map(GraphNodeAttribute.class::cast)
				.findAny();
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepRemoveField(graph);
	}
}