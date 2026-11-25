package nl.ou.refactoring.advice.nodes.code;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.code.GraphEdgeHas;
import nl.ou.refactoring.advice.edges.diagnosisresolutions.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDanger;

/**
 * Represents a node in a Refactoring Advice Graph that describes the affected programme code.
 */
public abstract class GraphNodeCode extends GraphNodeBase {
	/**
	 * Initialises a new instance of {@link GraphNodeCode}.
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeCode(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the code symbol that is represented by this node may be found at the specified program location.
	 * @param programLocationNode The node that represents the location in a software program where a code symbol can be found.
	 * @return The edge that indicates that the code symbol that is represented by this node may be found at the specified program location.
	 * @throws ArgumentNullException Thrown if programLocationNode is null.
	 */
	public GraphEdgeHas has(GraphNodeProgramLocation programLocationNode) throws ArgumentNullException {
		return
			this
				.graph
				.computeEdge(
					this,
					programLocationNode,
					(source, destination) -> new GraphEdgeHas(source, destination),
					GraphEdgeHas.class
				);
	}
	
	/**
	 * Gets the node that contains the program location of the code that is represented by this node.
	 * @return The node that contains the program location of the code that is represented by this node, wrapped in a {@link Optional<GraphNodeProgramLocation>}, or an empty {@link Optional<GraphNodeProgramLocation>} if not found.
	 */
	public Optional<GraphNodeProgramLocation> getProgramLocationNode() {
		return
			this
				.getEdges(GraphEdgeHas.class)
				.stream()
				.map(edge -> edge.getDestinationNode())
				.filter(GraphNodeProgramLocation.class::isInstance)
				.map(GraphNodeProgramLocation.class::cast)
				.findAny();
	}
	
	/**
	 * Gets all {@link GraphNodeDanger} nodes associated with this code element.
	 * @return The {@link GraphNodeDanger} nodes associated with this code element.
	 */
	public Set<GraphNodeDanger> getDangers() {
		return
				this
					.getEdgesIncoming(GraphEdgeAffects.class)
					.stream()
					.map(edge -> edge.getSourceNode())
					.filter(GraphNodeDanger.class::isInstance)
					.map(GraphNodeDanger.class::cast)
					.collect(Collectors.toUnmodifiableSet());
	}
}
