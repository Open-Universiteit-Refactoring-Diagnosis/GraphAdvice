package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeWorkflow;

/**
 * An edge that indicates that a refactoring is initiated with a particular Microstep.
 */
public final class GraphEdgeInitiates extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeInitiates}.
	 * @param start The start of a refactoring.
	 * @param workflowStep The first workflow step of a refactoring.
	 * @throws ArgumentNullException Thrown if start or microstep is null.
	 */
	public GraphEdgeInitiates(GraphNodeRefactoringStart start, GraphNodeWorkflow workflowStep)
			throws ArgumentNullException {
		super(start, workflowStep);
	}
}
