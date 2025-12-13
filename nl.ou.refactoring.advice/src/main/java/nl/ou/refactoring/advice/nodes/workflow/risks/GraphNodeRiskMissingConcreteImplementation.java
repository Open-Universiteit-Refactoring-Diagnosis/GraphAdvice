package nl.ou.refactoring.advice.nodes.workflow.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Missing Concrete Implementation" risk in a Refactoring Advice Graph.
 * This risk may arise if the method to be removed implements an abstract method
 * and removing it will cause a compilation error.
 */
public class GraphNodeRiskMissingConcreteImplementation extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskMissingConcreteImplementation}.
	 * @param graph {@link Graph} The graph that contains the risk.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskMissingConcreteImplementation(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates that the "Missing Concrete Implementation" affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Missing Concrete Implementation" risk and the affected Operation.
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
}
