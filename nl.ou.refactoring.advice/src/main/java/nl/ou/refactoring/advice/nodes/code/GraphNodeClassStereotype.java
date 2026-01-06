package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.resources.ResourceProvider;

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
	AFTER("after"),
	
	/**
	 * A "mitigated" class.
	 */
	MITIGATED("mitigated");
	
	private final String displayName;
	
	/**
	 * Instantiates a class stereotype value with displayName.
	 * @param displayName The display name of the class stereotype value.
	 */
	GraphNodeClassStereotype(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Gets the neutral display name for the class stereotype.
	 * @return The neutral display name for the class stereotype.
	 */
	public String getDisplayName() {
		return this.displayName;
	}
	
	/**
	 * Returns the localised display name of the class stereotype value.
	 */
	@Override
	public String toString() {
		return
			ResourceProvider
				.GraphNodeClassStereotypeDisplayNames
				.getDisplayName(this);
	}
}