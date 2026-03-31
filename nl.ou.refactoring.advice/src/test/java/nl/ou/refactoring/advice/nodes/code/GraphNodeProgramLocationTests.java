package nl.ou.refactoring.advice.nodes.code;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;

public final class GraphNodeProgramLocationTests {
	@DisplayName("Should get the appropriate caption of the program location")
	@Test
	public void getCaptionTest() {
		// Arrange
		final var fileNameFull = "$/tests/Test.java";
		final var fileName = "Test.java";
		final var lineNumberStart = 2;
		final var columnIndexStart = 20;
		final var lineNumberEnd = 5;
		final var columnIndexEnd = 2;
		final var captionExpected = "Test.java 2:20-5:2";
		final var graph = new Graph("Program Location Caption Test");
		final var programLocationNode =
			new GraphNodeProgramLocation(
				graph,
				fileNameFull,
				fileName,
				lineNumberStart,
				columnIndexStart,
				lineNumberEnd,
				columnIndexEnd
			);
		
		// Act
		final var captionActual = programLocationNode.getCaption();
		
		// Assert
		assertEquals(captionExpected, captionActual);
	}
}