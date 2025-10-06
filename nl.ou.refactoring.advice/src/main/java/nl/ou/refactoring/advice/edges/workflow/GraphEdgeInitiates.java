package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.GraphNodeRefactoringStart;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * An edge that indicates that a refactoring is initiated with a particular microstep.
 */
public final class GraphEdgeInitiates extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeInitiates}.
	 * @param start The start of a refactoring.
	 * @param microstep The first microstep of a refactoring.
	 * @throws ArgumentNullException Thrown if start or microstep is null.
	 */
	public GraphEdgeInitiates(GraphNodeRefactoringStart start, GraphNodeMicrostep microstep)
			throws ArgumentNullException {
		super(start, microstep);
	}
}
