package nl.ou.refactoring.advice.nodes.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;

/**
 * Represents an actionable node in a Refactoring Advice Graph workflow.
 */
public abstract class GraphNodeWorkflowAction extends GraphNodeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphNodeWorkflowAction}.
	 * 
	 * @param graph The graph that contains the node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeWorkflowAction(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	/**
	 * Indicates whether this workflow action is preceded by the specified workflow
	 * action.
	 * 
	 * @param workflowAction The workflow action that may or may not precede the
	 *                       current workflow action.
	 * @return True if the specified workflow action precedes the current workflow
	 *         action, false if not.
	 */
	public boolean isPrecededBy(GraphNodeWorkflowAction workflowAction) {
		return this.getPrecedingLength(workflowAction) > 0;
	}

	/**
	 * Indicates whether this workflow action is succeeded by the specified workflow
	 * action.
	 * 
	 * @param workflowAction The workflow action that may or may not succeed the
	 *                       current workflow action.
	 * @return True if the specified workflow action succeeds the current workflow
	 *         action, false if not.
	 */
	public boolean isSucceededBy(GraphNodeWorkflowAction workflowAction) {
		return this.getSucceedingLength(workflowAction) > 0;
	}

	/**
	 * Chains a workflow step to this workflow step. If the relationship already
	 * exists, no edges will be added to the graph and the existing edge will be
	 * returned.
	 * 
	 * @param next The next workflow step in the chain.
	 * @return An edge {@link GraphEdgePrecedes} that is connected to this node and
	 *         the next node.
	 * @throws ArgumentNullException Thrown if next is null.
	 */
	public GraphEdgePrecedes precedes(GraphNodeWorkflowAction next) throws ArgumentNullException {
		return this.graph.computeEdge(
				this,
				next,
				(sourceNode, destinationNode) -> new GraphEdgePrecedes(sourceNode, destinationNode),
				GraphEdgePrecedes.class);
	}
}
