package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;

/**
 * An edge that belongs to a workflow subgraph.
 */
public abstract class GraphEdgeWorkflow extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeWorkflow}.
	 * 
	 * @param sourceNode      The node preceding the destinationNode in the workflow
	 *                        subgraph.
	 * @param destinationNode The node that is next in the workflow subgraph.
	 * @throws ArgumentNullException Thrown if sourceNode or destinationNode is
	 *                               null.
	 */
	public GraphEdgeWorkflow(GraphNodeWorkflow sourceNode, GraphNodeWorkflow destinationNode)
			throws ArgumentNullException {
		super(sourceNode, destinationNode);
	}

	@Override
	public GraphNodeWorkflow getDestinationNode() {
		return (GraphNodeWorkflow) super.getDestinationNode();
	}

	@Override
	public GraphNodeWorkflow getSourceNode() {
		return (GraphNodeWorkflow) super.getSourceNode();
	}
}