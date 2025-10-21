package nl.ou.refactoring.advice.contracts;

import java.text.MessageFormat;

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
	
	/**
	 * Requires that a value cannot be less than the specified minimum.
	 * @param minimum The minimum value.
	 * @param value The actual value.
	 * @param parameterName The name of the parameter.
	 * @throws IllegalArgumentException Thrown if the value is not greater than or equal to the specified minimum.
	 */
	public static void requireGreaterThanOrEqual(double minimum, double value, String parameterName)
			throws IllegalArgumentException {
		if (value < minimum) {
			var messageFormat = "'{0}' cannot be less than {1} but was {2}";
			var message = MessageFormat.format(messageFormat, parameterName, minimum, value);
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Requires that a value cannot be less than the specified minimum.
	 * @param minimum The minimum value.
	 * @param value The actual value.
	 * @param parameterName The name of the parameter.
	 * @throws IllegalArgumentException Thrown if the value is not greater than or equal to the specified minimum.
	 */
	public static void requireGreaterThanOrEqual(int minimum, int value, String parameterName)
			throws IllegalArgumentException {
		if (value < minimum) {
			var messageFormat = "'{0}' cannot be less than {1} but was {2}";
			var message = MessageFormat.format(messageFormat, parameterName, minimum, value);
			throw new IllegalArgumentException(message);
		}
	}
}
