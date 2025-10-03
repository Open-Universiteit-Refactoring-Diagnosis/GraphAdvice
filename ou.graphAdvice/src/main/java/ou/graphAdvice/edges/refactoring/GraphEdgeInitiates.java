package ou.graphAdvice.edges.refactoring;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoringStart;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostep;

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
