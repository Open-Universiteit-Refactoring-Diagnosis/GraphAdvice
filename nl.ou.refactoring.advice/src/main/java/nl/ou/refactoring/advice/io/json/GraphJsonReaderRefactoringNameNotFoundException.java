package nl.ou.refactoring.advice.io.json;

/**
 * An exception that is thrown if the refactoring name of a Refactoring Advice Graph was not found in JSON.
 */
public final class GraphJsonReaderRefactoringNameNotFoundException extends GraphJsonReaderException {
	/**
	 * A generated serial version Unique Identifier.
	 */
	private static final long serialVersionUID = -3445066749495834861L;

	/**
	 * Initialises a new instance of {@link GraphJsonReaderRefactoringNameNotFoundException}.
	 */
	public GraphJsonReaderRefactoringNameNotFoundException() { }
	
	@Override
	public String getLocalizedMessage() {
		return "Did not find the refactoring name in the provided JSON";
	}
}
