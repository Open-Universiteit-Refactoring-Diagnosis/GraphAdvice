package nl.ou.refactoring.advice.nlp.tokens;

import nl.ou.refactoring.advice.nlp.NLPException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a syntax element could not be found by its token.
 */
public final class NLPTokenNotFoundException extends NLPException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -3694737447654162583L;
	
	/**
	 * The token of which the grammatical syntax element was not found.
	 */
	private final long token;
	
	/**
	 * The type of class that should be associated with the token.
	 */
	private final Class<?> classType;
	
	/**
	 * Initialises a new instance of {@link NLPTokenNotFoundException}.
	 * @param token The token of which the syntax element could not be found.
	 * @param classType The type of the Class that did not contain the requested syntax element with the specified token.
	 */
	public NLPTokenNotFoundException(long token, Class<?> classType) {
		this.token = token;
		this.classType = classType;
	}

	/**
	 * Gets the token of which the syntax element could not be found.
	 * @return The token of which the syntax element could not be found.
	 */
	public long getToken() {
		return this.token;
	}
	
	/**
	 * Gets the type of the Class that did not contain the requested syntax element with the specified token.
	 * @return {@link Class} The type of the Class that did not contain the requested syntax element with the specified token.
	 */
	public Class<?> getClassType() {
		return this.classType;
	}
	
	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(NLPTokenNotFoundException.class);
		return
			String.format(
				messageTemplate,
				this.token,
				this.classType.getName()
			);
	}
}