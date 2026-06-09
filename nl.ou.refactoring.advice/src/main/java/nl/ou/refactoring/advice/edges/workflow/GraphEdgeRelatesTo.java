package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * An edge in a Refactoring Advice Graph (RAG) that indicates that two workflow
 * nodes are directly related. Example: two microsteps that manipulate an
 * essentially equivalent code element.
 */
public final class GraphEdgeRelatesTo extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeRelatesTo}.
	 * 
	 * @param sourceNode      The source node.
	 * @param destinationNode The destination node.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is
	 *                               null.
	 */
	public GraphEdgeRelatesTo(GraphNodeMicrostep sourceNode, GraphNodeMicrostep destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}

	@Override
	public GraphNodeMicrostep getSourceNode() {
		return (GraphNodeMicrostep) super.getSourceNode();
	}

	@Override
	public GraphNodeMicrostep getDestinationNode() {
		return (GraphNodeMicrostep) super.getDestinationNode();
	}
}