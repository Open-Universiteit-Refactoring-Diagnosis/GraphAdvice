package nl.ou.refactoring.advice.edges.mechanics;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.mechanics.GraphNodeMechanic;

/**
 * An edge that belongs to a mechanics subgraph within a Refactoring Advice Graph (RAG).
 */
public abstract class GraphEdgeMechanic extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeMechanic}.
	 * 
	 * @param sourceNode      The node preceding the destinationNode in the workflow
	 *                        subgraph.
	 * @param destinationNode The node that is next in the workflow subgraph.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is
	 *                               null.
	 */
	public GraphEdgeMechanic(GraphNodeMechanic sourceNode, GraphNodeMechanic destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}

	@Override
	public GraphNodeMechanic getDestinationNode() {
		return (GraphNodeMechanic) super.getDestinationNode();
	}

	@Override
	public GraphNodeMechanic getSourceNode() {
		return (GraphNodeMechanic) super.getSourceNode();
	}
}