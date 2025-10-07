package nl.ou.refactoring.advice.edges.workflow;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * An Edge that indicates that a particular Node or collection of Nodes obsolesce another Node.
 */
public final class GraphEdgeObsolesces extends GraphEdge {

	/**
	 * Initialises a new instance of {@link GraphEdgeObsolesces}.
	 * @param microstep The Node that obsolesces the destination node.
	 * @param risk The Node that is obsolete.
	 * @throws ArgumentNullException Thrown if microstep or risk is null.
	 */
	public GraphEdgeObsolesces(GraphNodeMicrostep microstep, GraphNodeRisk risk) throws ArgumentNullException {
		super(microstep, risk);
	}
}