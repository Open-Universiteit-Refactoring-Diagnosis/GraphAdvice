package nl.ou.refactoring.advice.io;

import java.awt.Color;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Methods for {@link Color}.
 */
public final class ColorExtensions {

	private ColorExtensions() { }
	
	/**
	 * Converts a colour to its hexadecimal equivalent in a {@link String}.
	 * @param color The colour to convert.
	 * @return The hexadecimal equivalent of colour in a {@link String}.
	 * @throws ArgumentNullException Thrown if colour is null.
	 */
	public static String toHexadecimal(Color colour)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(colour, "colour");
		return String.format(
				"#%02x%02x%02x",
				colour.getRed(),
				colour.getGreen(),
				colour.getBlue()
		);
	}
}
