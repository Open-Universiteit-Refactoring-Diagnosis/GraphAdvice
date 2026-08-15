package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a Class specification could not be found in a
 * Refactoring Advice Graph (RAG).
 */
public final class AdviceApiCodeClassSpecificationNotFoundException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 2669550420811999262L;

	/**
	 * The Refactoring Advice Graph (RAG) that is missing the requested Class
	 * specification.
	 */
	private final Graph graph;

	/**
	 * The Fully Qualified Name (FQN) of the requested Class specification.
	 */
	private final String classFullyQualifiedName;

	/**
	 * Initialises a new instance of {@link AdviceApiCodeClassSpecificationNotFoundException}.
	 * 
	 * @param graph                   The Refactoring Advice Graph (RAG) that is
	 *                                missing the requested Class specification.
	 * @param classFullyQualifiedName The Fully Qualified Name (FQN) of the
	 *                                requested Class specification.
	 */
	public AdviceApiCodeClassSpecificationNotFoundException(Graph graph, String classFullyQualifiedName) {
		this.graph = graph;
		this.classFullyQualifiedName = classFullyQualifiedName;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages.getMessageTemplate(AdviceApiCodeClassSpecificationNotFoundException.class);
		return MessageFormat.format(messageTemplate, this.classFullyQualifiedName, this.graph.getRefactoringName());
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) that is missing the requested Class
	 * specification.
	 * 
	 * @return The Refactoring Advice Graph (RAG) that is missing the requested
	 *         Class specification.
	 */
	public Graph getGraph() {
		return this.graph;
	}

	/**
	 * Gets the Fully Qualified Name (FQN) of the requested Class specification.
	 * 
	 * @return The Fully Qualified Name (FQN) of the requested Class specification.
	 */
	public String getClassFullyQualifiedName() {
		return this.classFullyQualifiedName;
	}
}
