package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflowAction;

/**
 * An edge that indicates that a refactoring is finalised by a particular
 * workflow action.
 */
public final class GraphEdgeFinalises extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeFinalises}.
	 * 
	 * @param workflowAction The workflow action that finalises the refactoring.
	 * @param start          The start node of the refactoring.
	 * @throws ArgumentNullException Thrown if microstep or start is null.
	 */
	public GraphEdgeFinalises(GraphNodeWorkflowAction workflowAction, GraphNodeRefactoringStart start)
			throws ArgumentNullException {
		super(workflowAction, start);
	}

	@Override
	public GraphNodeRefactoringStart getDestinationNode() {
		return (GraphNodeRefactoringStart) super.getDestinationNode();
	}

	@Override
	public GraphNodeWorkflowAction getSourceNode() {
		return (GraphNodeWorkflowAction) super.getSourceNode();
	}
}