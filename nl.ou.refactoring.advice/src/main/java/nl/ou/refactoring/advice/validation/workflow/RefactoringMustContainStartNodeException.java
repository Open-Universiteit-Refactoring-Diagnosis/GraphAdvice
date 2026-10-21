package nl.ou.refactoring.advice.validation.workflow;

import nl.ou.refactoring.advice.resources.ResourceProvider;
import nl.ou.refactoring.advice.validation.GraphValidationException;

/**
 * An exception that is thrown if a graph does not contain a start node.
 */
public final class RefactoringMustContainStartNodeException extends GraphValidationException {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 8920594318172774458L;

	/**
	 * Initialises a new instance of {@link RefactoringMustContainStartNodeException}.
	 */
	public RefactoringMustContainStartNodeException() {
	}
	
	@Override
	public String getLocalizedMessage() {
		return
				ResourceProvider
					.ExceptionMessages
					.getMessageTemplate(this.getClass());
	}
}