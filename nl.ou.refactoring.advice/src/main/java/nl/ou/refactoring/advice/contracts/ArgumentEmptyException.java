package nl.ou.refactoring.advice.contracts;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * An exception that is thrown if an argument is empty.
 */
public final class ArgumentEmptyException extends IllegalArgumentException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = -6544698312811996884L;
	
	/**
	 * The name of the parameter that provided an empty argument.
	 */
	private final String parameterName;
	
	/**
	 * Initialises a new instance of {@link ArgumentEmptyException}.
	 * @param parameterName The name of the parameter that provided an empty argument.
	 */
	public ArgumentEmptyException(String parameterName) {
		super();
		this.parameterName = parameterName;
	}
	
	/**
	 * Gets the name of the parameter that provided an empty argument.
	 * @return The name of the parameter that provided an empty argument.
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
		final var messageFormat = ResourceBundle.getBundle("ExceptionMessages").getString("argumentEmpty");
		return MessageFormat.format(messageFormat, this.parameterName);
	}
}
