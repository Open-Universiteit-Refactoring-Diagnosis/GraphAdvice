package nl.ou.refactoring.advice.eclipse.refactorings;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * An event that is triggered if a {@link Graph} has changed.
 */
public final class GraphChangedEvent {
	private final Graph graph;
	
	/**
	 * Initialises a new instance of {@link GraphChangedEvent}.
	 * @param graph The changed {@link Graph}.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public GraphChangedEvent(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		this.graph = graph;
	}

	/**
	 * Gets the changed {@link Graph}.
	 * @return The changed {@link Graph}.
	 */
	public Graph getGraph() {
		return this.graph;
	}
}
