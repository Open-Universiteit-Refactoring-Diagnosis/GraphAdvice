package nl.ou.refactoring.advice.nodes.refactoring.risks;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.refactoring.GraphEdgeAffects;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;

/**
 * Represents a "Broken Sub-typing" risk in a Refactoring Advice Graph.
 * This risk may arise if a method is inserted into the inheritance / override hierarchy of a class's method and thus changes its behaviour.
 */
public final class GraphNodeRiskBrokenSubTyping extends GraphNodeRisk {
	/**
	 * Initialises a new instance of {@link GraphNodeRiskBrokenSubTyping}.
	 * @param graph {@link Graph} The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeRiskBrokenSubTyping(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
	
	/**
	 * Indicates that the "Broken Sub-typing" risk affects an Operation.
	 * @param operationNode The affected Operation.
	 * @return The edge that connects the "Broken Sub-typing" risk and the affected Operation.
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
