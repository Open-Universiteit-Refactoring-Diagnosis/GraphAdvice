package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * A microstep that adds a code element during a refactoring.
 */
public abstract class GraphNodeMicrostepAdd extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepAdd}.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG) that the microstep node
	 *              belongs to.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphNodeMicrostepAdd(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public abstract GraphNodeBase clone(Graph graph) throws ArgumentNullException;
}