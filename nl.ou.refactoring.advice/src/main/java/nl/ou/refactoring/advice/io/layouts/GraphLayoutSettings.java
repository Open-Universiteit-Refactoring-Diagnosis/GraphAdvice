package nl.ou.refactoring.advice.io.layouts;

/**
 * Settings for a graph layout.
 */
public abstract class GraphLayoutSettings {
	/**
	 * Initialises a new instance of {@link GraphLayoutSettings}.
	 */
	protected GraphLayoutSettings() { }
	
	/**
	 * Creates a graph layout from the settings.
	 * @return A graph layout with the current settings.
	 */
	public abstract GraphLayout createLayout();
}
