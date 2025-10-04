package ou.graphAdvice.nodes.refactoring.risks;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.refactoring.GraphEdgeAffects;
import ou.graphAdvice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Corresponding Subclass Specification risk in a Refactoring Advice Graph.
 * This risk may arise if a method is added and a method with the same signature exists in one or more subclasses.
 */
public final class GraphNodeRiskCorrespondingSubclassSpecification extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskCorrespondingSubclassSpecification}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskCorrespondingSubclassSpecification(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Corresponding Subclass Specification" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Corresponding Subclass Specification" risk and the affected Operation.
	 * @throws ArgumentNullException Thrown if operationNode is null.
	 */
	public GraphEdgeAffects affects(GraphNodeOperation operationNode)
			throws ArgumentNullException {
		return this.graph.addEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}
}
