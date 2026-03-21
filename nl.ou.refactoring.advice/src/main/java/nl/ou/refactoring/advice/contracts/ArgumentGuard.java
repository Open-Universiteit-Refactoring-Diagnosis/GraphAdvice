package nl.ou.refactoring.advice.contracts;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
	 * Requires that an item from an argument is in the specified collection.
	 * @param <T> The type of item.
	 * @param item The item to verify.
	 * @param itemParameterName The name of the parameter that provided the item argument.
	 * @param collection The collection to which the item should belong.
	 * @param collectionName The name of the collection to which the item should belong.
	 * @throws ArgumentNullException Thrown if item, itemParameterName, collection or collectionName is null.
	 * @throws ArgumentEmptyException Thrown if the item parameter name is empty or contains only white spaces.
	 * @throws ArgumentMembershipException Thrown if the specified item is not in the specified collection.
	 */
	public static <T> void requireItemOf(T item, String itemParameterName, Collection<T> collection, String collectionName)
			throws ArgumentNullException, ArgumentEmptyException, ArgumentMembershipException {
		requireNotNull(item, itemParameterName);
		requireNotNull(collection, "collection");
		requireNotNullEmptyOrWhiteSpace(collectionName, "collectionName");
		if (!collection.contains(item)) {
			throw new ArgumentMembershipException(
				(Object)item,
				collection
					.stream()
					.map(Object.class::cast)
					.collect(Collectors.toUnmodifiableSet()),
				collectionName
			);
		}
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
	 * Requires that a {@link String} argument matches the specified pattern.
	 * @param value The value that should match the specified pattern.
	 * @param pattern The pattern that should be matched.
	 * @param parameterName The name of the parameter that provides the value.
	 * @throws ArgumentPatternException Thrown if the value does not match the pattern.
	 */
	public static void requirePattern(String value, Pattern pattern, String parameterName)
			throws ArgumentPatternException {
		if (value == null) {
			throw new ArgumentPatternException(pattern, parameterName);
		}
		final var matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			throw new ArgumentPatternException(pattern, parameterName);
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
	public static void requireGreaterThanOrEqual(long minimum, long value, String parameterName)
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
