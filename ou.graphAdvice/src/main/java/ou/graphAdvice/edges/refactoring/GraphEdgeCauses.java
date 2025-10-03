package ou.graphAdvice.edges.refactoring;

import ou.graphAdvice.contracts.ArgumentNullException;
import ou.graphAdvice.edges.GraphEdge;
import ou.graphAdvice.nodes.refactoring.microsteps.GraphNodeMicrostep;
import ou.graphAdvice.nodes.refactoring.risks.GraphNodeRisk;

/**
 * An edge that indicates that a microstep causes a risk in a refactoring.
 */
public final class GraphEdgeCauses extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeCauses}.
	 * @param microstep The microstep that causes a risk.
	 * @param risk The risk that is caused by a microstep.
	 * @throws ArgumentNullException Thrown if microstep or risk is null.
	 */
	public GraphEdgeCauses(GraphNodeMicrostep microstep, GraphNodeRisk risk)
			throws ArgumentNullException {
		super(microstep, risk);
	}
}
