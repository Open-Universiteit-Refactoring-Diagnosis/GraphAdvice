package nl.ou.refactoring.advice.io.svg;

import java.awt.Rectangle;
import javax.xml.namespace.QName;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Settings for Scalable Vector Graphics (SVGs).
 */
public final class GraphSvgSettings {
	public static final QName SVG_NAMESPACE = new QName("http://www.w3.org/2000/svg");
	private int width = 1024;
	private int height = 768;
	private Rectangle viewBox = new Rectangle(this.width, this.height);
	
	/**
	 * Initialises a new instance of {@link GraphSvgSettings}.
	 */
	public GraphSvgSettings() { }

	/**
	 * Gets the width of the Scalable Vector Graphics (SVG) image.
	 * @return The width of the Scalable Vector Graphics (SVG) image.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * Sets the width of the Scalable Vector Graphics (SVG) image.
	 * @param value The new width of the Scalable Vector Graphics (SVG) image.
	 * @throws IllegalArgumentException Thrown if value is less than zero (0).
	 */
	public void setWidth(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, value, "width");
		this.width = value;
	}
	
	/**
	 * Gets the height of the Scalable Vector Graphics (SVG) image.
	 * @return The height of the Scalable Vector Graphics (SVG) image.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Sets the height of the Scalable Vector Graphics (SVG) image.
	 * @param value The new height of the Scalable Vector Graphics (SVG) image.
	 * @throws IllegalArgumentException Thrown if value is less than zero (0).
	 */
	public void setHeight(int value)
			throws IllegalArgumentException {
		ArgumentGuard.requireGreaterThanOrEqual(0, value, "height");
		this.height = value;
	}
	
	/**
	 * Gets the view box of the Scalable Vector Graphics (SVG) image.
	 * @return The view box of the Scalable Vector Graphics (SVG) image.
	 */
	public Rectangle getViewBox() {
		return this.viewBox;
	}
	
	/**
	 * Sets the view box of the Scalable Vector Graphics (SVG) image.
	 * @param value The new view box of the Scalable Vector Graphics (SVG) image.
	 * @throws ArgumentNullException Thrown if value is null.
	 */
	public void setViewBox(Rectangle value)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(value, "viewBox");
		this.viewBox = value;
	}
}
