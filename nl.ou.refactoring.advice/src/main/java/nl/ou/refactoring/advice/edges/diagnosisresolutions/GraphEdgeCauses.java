package nl.ou.refactoring.advice.edges.diagnosisresolutions;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers.GraphNodeDanger;
import nl.ou.refactoring.advice.nodes.mechanics.microsteps.GraphNodeMicrostep;

/**
 * An edge that indicates that a Microstep causes a risk in a refactoring.
 */
public final class GraphEdgeCauses extends GraphEdge {
	/**
	 * Initialises a new instance of {@link GraphEdgeCauses}.
	 * 
	 * @param microstep The microstep that causes a risk.
	 * @param risk      The risk that is caused by a microstep.
	 * @throws ArgumentNullException Thrown if microstep or risk is null.
	 */
	public GraphEdgeCauses(GraphNodeMicrostep microstep, GraphNodeDanger risk) throws ArgumentNullException {
		super(microstep, risk);
	}

	@Override
	public GraphNodeDanger getDestinationNode() {
		return (GraphNodeDanger) super.getDestinationNode();
	}

	@Override
	public GraphNodeMicrostep getSourceNode() {
		return (GraphNodeMicrostep) super.getSourceNode();
	}
}
