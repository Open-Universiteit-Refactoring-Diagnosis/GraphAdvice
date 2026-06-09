package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * A microstep node in a Refactoring Advice Graph (RAG) that removes a code element.
 */
public abstract class GraphNodeMicrostepRemove extends GraphNodeMicrostep {
	/**
	 * Initialises a new instance of {@link GraphNodeMicrostepRemove}.
	 * @param graph The Refactoring Advice Graph (RAG) to which the microstep node belongs.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphNodeMicrostepRemove(Graph graph) throws ArgumentNullException {
		super(graph);
	}

	@Override
	public abstract GraphNodeBase clone(Graph graph) throws ArgumentNullException;
}
