package nl.ou.refactoring.advice.api;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a refactoring template is not defined.
 */
public final class RefactoringTemplateUndefinedException extends AdviceApiException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 3610453851275565916L;

	/**
	 * The undefined template.
	 */
	private final RefactoringTemplateIdentifier template;

	/**
	 * Initialises a new instance of {@link RefactoringTemplateUndefinedException}.
	 * 
	 * @param template The {@link RefactoringTemplateIdentifier} that is not
	 *                 defined.
	 */
	public RefactoringTemplateUndefinedException(RefactoringTemplateIdentifier template) {
		this.template = template;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
				ResourceProvider.ExceptionMessages.getMessageTemplate(RefactoringTemplateUndefinedException.class);
		return MessageFormat.format(messageTemplate, this.template.toString());
	}

	/**
	 * Gets the {@link RefactoringTemplateIdentifier} that is not defined.
	 * 
	 * @return The {@link RefactoringTemplateIdentifier} that is not defined.
	 */
	public RefactoringTemplateIdentifier getTemplate() {
		return this.template;
	}
}