package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;

/**
 * An Edge that indicates that a Workflow Step precedes another Workflow Step in a refactoring.
 */
public final class GraphEdgePrecedes extends GraphEdgeWorkflow {
	/**
	 * Initialises a new instance of {@link GraphEdgePrecedes}.
	 * @param currentWorkflowAction The current workflow action.
	 * @param nextWorkflowAction The next current workflow action.
	 * @throws ArgumentNullException Thrown if currentWorkflowStep or nextWorkflowStep is null.
	 */
	public GraphEdgePrecedes(GraphNodeWorkflow currentWorkflowAction, GraphNodeWorkflow nextWorkflowAction)
			throws ArgumentNullException {
		super(currentWorkflowAction, nextWorkflowAction);
	}
	
	@Override
	public GraphNodeWorkflow getSourceNode() {
		return (GraphNodeWorkflow)super.getSourceNode();
	}
	
	@Override
	public GraphNodeWorkflow getDestinationNode() {
		return (GraphNodeWorkflow)super.getDestinationNode();
	}
}
