package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflowAction;

/**
 * An Edge that indicates that a Workflow Step precedes another Workflow Step in a refactoring.
 */
public final class GraphEdgePrecedes extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgePrecedes}.
	 * @param currentWorkflowAction The current workflow action.
	 * @param nextWorkflowAction The next current workflow action.
	 * @throws ArgumentNullException Thrown if currentWorkflowStep or nextWorkflowStep is null.
	 */
	public GraphEdgePrecedes(GraphNodeWorkflowAction currentWorkflowAction, GraphNodeWorkflowAction nextWorkflowAction)
			throws ArgumentNullException {
		super(currentWorkflowAction, nextWorkflowAction);
	}
	
	@Override
	public GraphNodeWorkflowAction getSourceNode() {
		return (GraphNodeWorkflowAction)super.getSourceNode();
	}
	
	@Override
	public GraphNodeWorkflowAction getDestinationNode() {
		return (GraphNodeWorkflowAction)super.getDestinationNode();
	}
	
	@Override
	public String getLabel() {
		return "Precedes";
	}
}
