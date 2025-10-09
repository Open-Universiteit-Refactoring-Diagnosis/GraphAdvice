package nl.ou.refactoring.advice.drawing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class GraphPainterTests {
	
	@Test
	@DisplayName("Should draw a Refactoring Advice Graph on a canvas")
	public void drawTest() {
		// Arrange graph
		final var graph = new Graph();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		addMethod.precedes(updateReferences);
		addMethod.obsolesces(missingDefinition);
		updateReferences.precedes(removeMethod);
		updateReferences.obsolesces(missingDefinition);
		removeMethod.causes(missingDefinition);
		
		// Arrange graphics
		final var width = 1024;
		final var height = 768;
		final var graphPainter = new GraphPainter(width, height);
		final var graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final var graphics = graphImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Act
		graphPainter.draw(graph, graphics);
		
		// Assert
		assertEquals(width, graphImage.getWidth());
		assertEquals(height, graphImage.getHeight());
		// compare to reference image
		
		try {
			final var file = new File("C:\\Temp\\Graph.png");
			file.delete();
			file.createNewFile();
			ImageIO.write(graphImage, "png", file);
		} catch (IOException exception) {
			fail("Failed to write test image");
		}
	}
}
