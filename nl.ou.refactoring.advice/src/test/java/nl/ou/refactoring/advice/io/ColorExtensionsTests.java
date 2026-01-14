package nl.ou.refactoring.advice.io;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static nl.ou.refactoring.advice.io.ColorExtensions.toHexadecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ColorExtensionsTests {
	@ParameterizedTest
	@MethodSource("toHexTestCases")
	@DisplayName("Should convert colours to their hexadecimal equivalent in a string")
	public void toHexadecimalTest(Color colour, String expected) {
		final var actual = toHexadecimal(colour);
		assertEquals(expected, actual);
	}
	
	private static Stream<Arguments> toHexTestCases() {
		final List<Arguments> arguments = new ArrayList<Arguments>();
		
		final var colourBlue = Color.blue;
		final var colourBlueHex = "#0000ff";
		arguments.add(Arguments.of(colourBlue, colourBlueHex));
		
		final var colourRed = Color.red;
		final var colourRedHex = "#ff0000";
		arguments.add(Arguments.of(colourRed, colourRedHex));
		
		final var colourGreen = Color.green;
		final var colourGreenHex = "#00ff00";
		arguments.add(Arguments.of(colourGreen, colourGreenHex));
		
		return arguments.stream();
	}
}