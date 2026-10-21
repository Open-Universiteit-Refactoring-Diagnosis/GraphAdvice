package nl.ou.refactoring.advice.validation.workflow;

import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.GraphValidationException;

/**
 * An exception that is thrown if a graph contains more than one end node.
 */
public final class RefactoringMayContainOnlyOneEndNodeException extends GraphValidationException {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -8167416037244414474L;

	/**
	 * Initialises a new instance of
	 * {@link RefactoringMayContainOnlyOneEndNodeException}.
	 */
	public RefactoringMayContainOnlyOneEndNodeException() {
	}

	@Override
	public String getLocalizedMessage() {
		return ResourceProvider.ExceptionMessages
				.getMessageTemplate(RefactoringMayContainOnlyOneEndNodeException.class);
	}
}