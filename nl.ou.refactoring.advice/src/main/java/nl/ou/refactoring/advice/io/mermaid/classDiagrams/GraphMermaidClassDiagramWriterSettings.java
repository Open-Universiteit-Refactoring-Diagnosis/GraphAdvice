package nl.ou.refactoring.advice.io.mermaid.classDiagrams;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;

/**
 * Settings for a {@link GraphMermaidClassDiagramWriter}.
 */
public class GraphMermaidClassDiagramWriterSettings {
	public static final int CODE_DANGER_PATH_MAXIMUM_DEPTH_DEFAULT = 10;
	private int codeDangerPathMaximumDepth = CODE_DANGER_PATH_MAXIMUM_DEPTH_DEFAULT;
	
	/**
	 * Initialises a new instance of {@link GraphMermaidClassDiagramWriterSettings}.
	 */
	public GraphMermaidClassDiagramWriterSettings() { }

	/**
	 * Gets the maximum depth of the paths that check for dangers on a code element.
	 * @return The maximum depth of the paths that check for dangers on a code element.
	 */
	public int getCodeDangerPathMaximumDepth() {
		return this.codeDangerPathMaximumDepth;
	}
	
	/**
	 * Sets the maximum depth of the paths that check for dangers on a code element.
	 * @param value The new maximum depth of the paths that check for dangers on a code element.
	 * @throws IllegalArgumentException Thrown if the value is less than three (3).
	 */
	public void setCodeDangerPathMaximumDepth(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(3, value, "codeDangerPathMaximumDepth");
		this.codeDangerPathMaximumDepth = value;
	}
}
