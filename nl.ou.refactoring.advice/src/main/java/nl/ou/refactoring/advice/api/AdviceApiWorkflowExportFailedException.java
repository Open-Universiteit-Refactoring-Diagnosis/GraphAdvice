package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a workflow export from a Refactoring Advice
 * Graph (RAG) failed.
 */
public final class AdviceApiWorkflowExportFailedException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 7031972950967385833L;

	/**
	 * The Refactoring Advice Graph (RAG) from which the workflow export failed.
	 */
	private final Graph graph;

	/**
	 * Initialises a new instance of {@link AdviceApiWorkflowExportFailedException}.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG) from which the workflow
	 *              export failed.
	 * @param cause The cause of the exception.
	 */
	public AdviceApiWorkflowExportFailedException(Graph graph, Throwable cause) {
		super(cause);
		this.graph = graph;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages.getMessageTemplate(AdviceApiWorkflowExportFailedException.class);
		return MessageFormat.format(messageTemplate, this.graph.getRefactoringName());
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) from which the workflow export
	 * failed.
	 * 
	 * @return
	 */
	public Graph getGraph() {
		return this.graph;
	}
}