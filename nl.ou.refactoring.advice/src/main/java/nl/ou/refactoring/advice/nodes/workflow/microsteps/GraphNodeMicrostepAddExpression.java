package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds an Expression.
 */
public final class GraphNodeMicrostepAddExpression extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddExpression}.
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddExpression(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}
}