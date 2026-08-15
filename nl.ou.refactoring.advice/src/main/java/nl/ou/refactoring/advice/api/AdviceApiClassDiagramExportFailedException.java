package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if the export of a UML Class Diagram from a
 * Refactoring Advice Graph (RAG) failed.
 */
public final class AdviceApiClassDiagramExportFailedException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 2694119402824259545L;

	/**
	 * The Refactoring Advice Graph (RAG) that could not export a UML Class Diagram.
	 */
	private final Graph graph;

	/**
	 * Initialises a new instance of {@link AdviceApiClassDiagramExportFailedException}.
	 * 
	 * @param cause The cause of the exception.
	 */
	AdviceApiClassDiagramExportFailedException(Graph graph, Throwable cause) {
		super(cause);
		this.graph = graph;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages.getMessageTemplate(AdviceApiClassDiagramExportFailedException.class);
		return MessageFormat.format(messageTemplate, this.graph.getRefactoringName());
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) that could not export a UML Class
	 * Diagram.
	 * 
	 * @return The Refactoring Advice Graph (RAG) that could not export a UML Class
	 *         Diagram.
	 */
	public Graph getGraph() {
		return this.graph;
	}
}