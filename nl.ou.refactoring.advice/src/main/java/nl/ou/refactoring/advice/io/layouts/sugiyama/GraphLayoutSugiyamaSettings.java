package nl.ou.refactoring.advice.io.layouts.sugiyama;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.io.layouts.GraphLayout;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutSettings;

/**
 * Settings for a Sugiyama (Layered) Layout of a Refactoring Advice Graph.
 */
public final class GraphLayoutSugiyamaSettings extends GraphLayoutSettings {
	private double layerHeight = 100.0;
	private double nodeSpacing = 80.0;
	
	/**
	 * Initialises a new instance of {@link GraphLayoutSugiyamaSettings}.
	 */
	public GraphLayoutSugiyamaSettings() { }

	@Override
	public GraphLayout createLayout() {
		return new GraphLayoutSugiyama(this);
	}
	
	/**
	 * Gets the layer height.
	 * @return The layer height.
	 */
	public double getLayerHeight() {
		return this.layerHeight;
	}
	
	/**
	 * Sets the layer height.
	 * @param value The new layer height.
	 * @throws IllegalArgumentException Thrown if the new value is less than zero.
	 */
	public void setLayerHeight(double value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, value, "layerHeight");
		this.layerHeight = value;
	}
	
	/**
	 * Gets the spacing between nodes.
	 * @return The spacing between nodes.
	 */
	public double getNodeSpacing() {
		return this.nodeSpacing;
	}

	/**
	 * Sets the spacing between nodes.
	 * @param value The new spacing between nodes.
	 * @throws IllegalArgumentException Thrown if the new value is less than zero.
	 */
	public void setNodeSpacing(double value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(80.0, value, "nodeSpacing");
		this.nodeSpacing = value;
	}
}
