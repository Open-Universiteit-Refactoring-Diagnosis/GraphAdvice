package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * Represents a Microstep in a Refactoring Advice Graph that removes a Method
 * Body.
 */
public final class GraphNodeMicrostepRemoveMethodBody extends GraphNodeMicrostepRemove {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemoveMethodBody}.
	 * 
	 * @param graph The graph that contains the microstep. Cannot be null.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemoveMethodBody(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeMicrostepRemoveMethodBody(graph);
	}
}