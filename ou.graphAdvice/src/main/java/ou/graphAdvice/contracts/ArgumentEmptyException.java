package ou.graphAdvice.contracts;

import java.text.MessageFormat;

/**
 * An exception that is thrown if an argument is empty.
 */
public final class ArgumentEmptyException extends IllegalArgumentException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = -6544698312811996884L;
	private final String parameterName;
	
	/**
	 * Initialises a new instance of {@link ArgumentEmptyException}.
	 * @param parameterName The name of the paraneter that provided an empty argument.
	 */
	public ArgumentEmptyException(String parameterName) {
		super(MessageFormat.format("'{0}' cannot be empty.", parameterName));
		this.parameterName = parameterName;
	}
	
	/**
	 * Gets the name of the parameter that provided an empty argument.
	 * @return The name of the parameter that provided an empty argument.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
}
