package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * An edge that indicates that a refactoring is finalised by a particular microstep.
 */
public final class GraphEdgeFinalises extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeFinalises}.
	 * @param microstep The microstep that finalises the refactoring.
	 * @param start The start node of the refactoring.
	 * @throws ArgumentNullException Thrown if microstep or start is null.
	 */
	public GraphEdgeFinalises(GraphNodeMicrostep microstep, GraphNodeRefactoringStart start)
			throws ArgumentNullException {
		super(microstep, start);
	}
}
