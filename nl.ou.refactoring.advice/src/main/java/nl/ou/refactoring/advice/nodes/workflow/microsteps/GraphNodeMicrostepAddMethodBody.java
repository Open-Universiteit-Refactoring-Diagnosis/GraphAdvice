package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * Represents a Microstep in a Refactoring Advice Graph that adds a Method Body.
 */
public final class GraphNodeMicrostepAddMethodBody extends GraphNodeMicrostepAdd {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAddMethodBody}.
	 * 
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepAddMethodBody(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepAddMethodBody(graph);
	}

}