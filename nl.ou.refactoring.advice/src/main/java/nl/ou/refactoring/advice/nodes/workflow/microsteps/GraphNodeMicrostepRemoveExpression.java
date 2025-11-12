package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes an Expression.
 */
public final class GraphNodeMicrostepRemoveExpression extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveExpression}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveExpression(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public String getCaption() {
		return "Remove Expression";
	}
}