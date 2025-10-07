package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * An edge that indicates that a Microstep causes a risk in a refactoring.
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
