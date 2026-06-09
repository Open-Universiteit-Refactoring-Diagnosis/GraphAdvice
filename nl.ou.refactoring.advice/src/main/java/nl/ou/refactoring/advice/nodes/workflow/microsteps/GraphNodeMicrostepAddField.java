package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAdds;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Field.
 */
public final class GraphNodeMicrostepAddField extends GraphNodeMicrostepAdd {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddField}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddField(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Add Field" microstep adds the attribute represented by attributeNode.
	 * @param attributeNode The attribute node that is added by this microstep.
	 * @return The edge that indicates that the "Add Field" microstep adds the attribute represented by attributeNode.
	 * @throws ArgumentNullException Thrown if attributeNode is null.
	 */
	public GraphEdgeAdds adds(GraphNodeAttribute attributeNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(attributeNode, "attributeNode");
		return
			this
				.graph
				.computeEdge(
					this,
					attributeNode,
					(source, destination) -> new GraphEdgeAdds(source, destination),
					GraphEdgeAdds.class
				);
	}
	
	/**
	 * Gets the {@link GraphNodeAttribute} node that represents the attribute that is added by the microstep.
	 * @return The {@link GraphNodeAttribute} node that represents the attribute that is added by the microstep wrapped in {@link Optional<GraphNodeAttribute>}, or an empty {@link Optional<GraphNodeAttribute>} if there is none.
	 */
	public Optional<GraphNodeAttribute> getAttributeNode() {
		return
			this
				.getEdges(GraphEdgeAdds.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(node -> node instanceof GraphNodeAttribute)
				.map(GraphNodeAttribute.class::cast)
				.findAny();
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepAddField(graph);
	}
}