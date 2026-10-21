package nl.ou.refactoring.advice.validation.workflow;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.GraphValidationException;

/**
 * An exception that is thrown if there is an attempt to modify a Refactoring
 * Advice Graph (RAG)'s workflow subgraph when it has already been finalised.
 */
public final class GraphIsFinalisedException extends GraphValidationException {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 3690093794212294381L;

	/**
	 * The finalised Refactoring Advice {@link Graph} (RAG).
	 */
	private final Graph graph;

	/**
	 * Initialises a new instance of {@link GraphIsFinalisedException}.
	 */
	public GraphIsFinalisedException(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Gets the finalised Refactoring Advice {@link Graph} (RAG).
	 * 
	 * @return The finalised Refactoring Advice {@link Graph} (RAG).
	 */
	public Graph getGraph() {
		return this.graph;
	}

	@Override
	public String getLocalizedMessage() {
		return ResourceProvider.ExceptionMessages.getMessageTemplate(GraphIsFinalisedException.class);
	}
}