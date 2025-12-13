package nl.ou.refactoring.advice.contracts;

import java.text.MessageFormat;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if an argument is null.
 */
public final class ArgumentNullException extends NullPointerException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = 2670122099172288318L;
	
	/**
	 * The name of the parameter that provided a null argument.
	 */
	private final String parameterName;
	
	/**
	 * Initialises a new instance of {@link ArgumentNullException}.
	 * @param parameterName The name of the parameter that provided a null argument.
	 */
	public ArgumentNullException(String parameterName) {
		super();
		this.parameterName = parameterName;
	}
	
	/**
	 * Gets the name of the parameter that provided a null argument.
	 * @return The name of the parameter that provided a null argument.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
	
	/**
	 * Gets a localised exception message that contains relevant details.
	 * @return A localised exception message that contains relevant details.
	 */
	@Override
	public String getLocalizedMessage() {
		final var messageFormat =
				ResourceProvider
					.ExceptionMessages
					.getMessageTemplate(this.getClass());
		return MessageFormat.format(messageFormat, this.parameterName);
	}
}
