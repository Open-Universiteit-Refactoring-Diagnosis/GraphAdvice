package nl.ou.refactoring.advice.contracts;

public final class ArgumentGuard {
	/**
	 * Requires that an argument is not null.
	 * @param value The argument value to check.
	 * @param parameterName The name of the parameter from which the argument originates.
	 * @throws ArgumentNullException Thrown if the argument is null.
	 */
	public static void requireNotNull(Object value, String parameterName)
			throws ArgumentNullException {
		if (value == null) {
			throw new ArgumentNullException(parameterName);
		}
	}
	
	/**
	 * Requires that a {@link String} argument is not null or empty and does not contain only white spaces.
	 * @param value The argument value to check.
	 * @param parameterName The name of the parameter from which the argument originates.
	 * @throws ArgumentNullException Thrown if the argument is null.
	 * @throws ArgumentEmptyException Thrown if the argument is empty or contains only white spaces.
	 */
	public static void requireNotNullEmptyOrWhiteSpace(String value, String parameterName)
			throws ArgumentNullException, ArgumentEmptyException {
		if (value == null) {
			throw new ArgumentNullException(parameterName);
		}
		if (value.trim().isEmpty()) {
			throw new ArgumentEmptyException(parameterName);
		}
	}
}
