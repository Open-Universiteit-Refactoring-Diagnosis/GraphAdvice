package nl.ou.refactoring.advice.nodes.code;

/**
 * The stereotype of a class in a refactoring advice Class Diagram.
 */
public enum GraphNodeClassStereotype {
	/**
	 * A "before" class.
	 */
	BEFORE("before"),
	
	/**
	 * An "after" class.
	 */
	AFTER("after");
	
	private final String displayName;
	
	/**
	 * Instantiates an enum value with displayName.
	 * @param displayName The display name of the enum value.
	 */
	GraphNodeClassStereotype(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Returns the display name of the enum value.
	 */
	@Override
	public String toString() {
		return this.displayName;
	}
}