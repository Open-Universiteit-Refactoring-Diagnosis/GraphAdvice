package nl.ou.refactoring.advice.validation.workflow;

import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.GraphValidationException;

/**
 * An exception that is thrown if a graph does not contain an end node.
 */
public final class RefactoringMustContainEndNodeException extends GraphValidationException {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 8828886549372049782L;

	/**
	 * Initialises a new instance of {@link RefactoringMustContainEndNodeException}.
	 */
	public RefactoringMustContainEndNodeException() {
	}

	@Override
	public String getLocalizedMessage() {
		return ResourceProvider.ExceptionMessages.getMessageTemplate(RefactoringMustContainEndNodeException.class);
	}
}
