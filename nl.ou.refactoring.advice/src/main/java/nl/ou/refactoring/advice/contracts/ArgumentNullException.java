package nl.ou.refactoring.advice.contracts;

import java.text.MessageFormat;

/**
 * An exception that is thrown if an argument is null.
 */
public final class ArgumentNullException extends IllegalArgumentException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = 2670122099172288318L;
	private final String parameterName;
	
	/**
	 * Initialises a new instance of {@link ArgumentNullException}.
	 * @param parameterName The name of the parameter that provided a null argument.
	 */
	public ArgumentNullException(String parameterName) {
		super(MessageFormat.format("'{0}' cannot be null", parameterName));
		this.parameterName = parameterName;
	}
	
	/**
	 * Gets the name of the parameter that provided a null argument.
	 * @return The name of the parameter that provided a null argument.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
}
