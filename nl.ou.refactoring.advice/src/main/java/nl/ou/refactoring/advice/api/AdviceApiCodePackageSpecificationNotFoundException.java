package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a package specification could not be found in
 * a Refactoring Advice Graph (RAG).
 */
public final class AdviceApiCodePackageSpecificationNotFoundException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -6466386509561858897L;

	/**
	 * The Refactoring Advice Graph (RAG) that is missing the requested package
	 * specification.
	 */
	private final Graph graph;

	/**
	 * The name of the requested package specification.
	 */
	private final String packageName;

	/**
	 * Initialises a new instance of
	 * {@link AdviceApiCodePackageSpecificationNotFoundException}.
	 * 
	 * @param graph       The Refactoring Advice Graph (RAG) that is missing the
	 *                    requested package specification.
	 * @param packageName The name of the requested package specification.
	 */
	public AdviceApiCodePackageSpecificationNotFoundException(Graph graph, String packageName) {
		this.graph = graph;
		this.packageName = packageName;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages
						.getMessageTemplate(AdviceApiCodePackageSpecificationNotFoundException.class);
		return MessageFormat.format(messageTemplate, this.packageName, this.graph.getRefactoringName());
	}

	/**
	 * Gets the Refactoring Advice Graph (RAG) that is missing the requested package
	 * specification.
	 * 
	 * @return The Refactoring Advice Graph (RAG) that is missing the requested
	 *         package specification.
	 */
	public Graph getGraph() {
		return this.graph;
	}

	/**
	 * Gets the name of the requested package specification.
	 * 
	 * @return The name of the requested package specification.
	 */
	public String getPackageName() {
		return this.packageName;
	}
}