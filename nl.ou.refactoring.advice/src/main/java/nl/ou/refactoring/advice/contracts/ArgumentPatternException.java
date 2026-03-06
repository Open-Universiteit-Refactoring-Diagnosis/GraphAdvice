package nl.ou.refactoring.advice.contracts;

import java.util.regex.Pattern;

/**
 * An exception that is thrown if an argument does not match an expected pattern.
 */
public final class ArgumentPatternException extends IllegalArgumentException {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = 7256975406358054346L;
	
	/**
	 * The expected pattern for the argument.
	 */
	private final Pattern pattern;
	
	/**
	 * The name of the parameter that does not match the pattern.
	 */
	private final String parameterName;

	/**
	 * Initialises a new instance of {@link ArgumentPatternException}.
	 * @param pattern The pattern that must be valid on the argument value.
	 * @param parameterName The name of the parameter that supplies the argument value.
	 */
	public ArgumentPatternException(Pattern pattern, String parameterName) {
		this.pattern = pattern;
		this.parameterName = parameterName;
	}

	/**
	 * Gets the expected pattern for the argument.
	 * @return The expected pattern for the argument.
	 */
	public Pattern getPattern() {
		return this.pattern;
	}
	
	/**
	 * Gets the name of the parameter that does not match the pattern.
	 * @return The name of the parameter that does not match the pattern.
	 */
	public String getParameterName() {
		return this.parameterName;
	}
}