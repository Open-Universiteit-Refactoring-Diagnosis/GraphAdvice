package nl.ou.refactoring.advice.io.json;

/**
 * An exception that is thrown if a specified node class was not found.
 */
public final class GraphJsonReaderNodeClassNotFoundException extends GraphJsonReaderException {
	/**
	 * A generated serial version Unique Identifier.
	 */
	private static final long serialVersionUID = 5493974119813085154L;
	
	private final String className;

	/**
	 * Initialises a new instance of {@link GraphJsonReaderNodeClassNotFoundException}.
	 * @param className The class name of the Graph Node.
	 */
	public GraphJsonReaderNodeClassNotFoundException(String className) {
		this.className = className;
	}
	
	@Override
	public String getLocalizedMessage() {
		return String.format("Graph node of class type '%s' not found", this.className);
	}
}
