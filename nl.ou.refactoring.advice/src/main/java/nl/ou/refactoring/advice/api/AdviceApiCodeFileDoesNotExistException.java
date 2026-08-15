package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a code file that should be read to import
 * Abstract Syntax Tree (AST) elements into a Refactoring Advice Graph (RAG)
 * does not exist.
 */
public final class AdviceApiCodeFileDoesNotExistException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -620532155449083163L;

	/**
	 * The Refactoring Advice Graph (RAG) that should contain the Abstract Syntax
	 * Tree (AST) elements from the code file.
	 */
	private final Graph graph;

	/**
	 * The file name (path to the file) of the file that does not exist.
	 */
	private final String fileName;

	public AdviceApiCodeFileDoesNotExistException(Graph graph, String fileName) {
		this.graph = graph;
		this.fileName = fileName;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages.getMessageTemplate(AdviceApiCodeFileDoesNotExistException.class);
		return MessageFormat.format(messageTemplate, fileName, graph.getRefactoringName());
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) that should contain the Abstract
	 * Syntax Tree (AST) elements from the code file.
	 * 
	 * @return The Refactoring Advice Graph (RAG) that should contain the Abstract
	 *         Syntax Tree (AST) elements from the code file.
	 */
	public Graph getGraph() {
		return this.graph;
	}

	/**
	 * Gets the file name (path to the file) of the file that does not exist.
	 * 
	 * @return The file name (path to the file) of the file that does not exist.
	 */
	public String getFileName() {
		return this.fileName;
	}
}
