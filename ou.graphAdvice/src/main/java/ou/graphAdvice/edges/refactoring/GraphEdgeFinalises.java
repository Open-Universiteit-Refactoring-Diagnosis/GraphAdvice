package ou.graphAdvice.edges.refactoring;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.nodes.refactoring.GraphNodeRefactoringStart;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostep;

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
