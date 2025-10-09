package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

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
		return this.graph.getOrAddEdge(
				this,
				operationNode,
				(source, destination) -> new GraphEdgeAffects(source, destination),
				GraphEdgeAffects.class);
	}

	@Override
	public String getCaption() {
		return "Corresponding Subclass Specification";
	}
}
