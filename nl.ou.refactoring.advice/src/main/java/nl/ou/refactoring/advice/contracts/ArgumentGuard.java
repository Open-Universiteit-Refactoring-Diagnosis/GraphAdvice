package nl.ou.refactoring.advice.contracts;

/**
 * Enforces contracts on parameter arguments in methods.
 */
public final class ArgumentGuard {
	/**
	 * A private constructor to avoid instantiation.
	 * This class is only for static methods.
	 */
	private ArgumentGuard() {
		throw new AssertionError("ArgumentGuard should not be instantiated; it only hosts static methods.");
	}
	
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
