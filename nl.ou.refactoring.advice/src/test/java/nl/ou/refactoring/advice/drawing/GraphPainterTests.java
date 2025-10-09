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
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepUpdateReferences;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyRenameConflictingSymbol;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class GraphPainterTests {
	
	@Test
	@DisplayName("Should draw a Refactoring Advice Graph on a canvas")
	public void drawTest() throws RefactoringMayContainOnlyOneStartNodeException {
		// Arrange graph
		final var graph = new Graph("Move Method");
		
		// Arrange graph code
		final var packageRefactoring = new GraphNodePackage(graph, "ou.refactoring");
		final var classAlpha = new GraphNodeClass(graph, "Alpha");
		final var operationAlphaAbc = new GraphNodeOperation(graph, "abc");
		final var attributeAlphaFoo = new GraphNodeAttribute(graph, "foo");
		final var attributeAlphaBar = new GraphNodeAttribute(graph, "bar");
		packageRefactoring.has(classAlpha);
		classAlpha.has(operationAlphaAbc);
		classAlpha.has(attributeAlphaFoo);
		classAlpha.has(attributeAlphaBar);
		final var classBeta = new GraphNodeClass(graph, "Beta");
		final var operationBetaAbc2 = new GraphNodeOperation(graph, "abc2");
		final var attributeBetaMyField = new GraphNodeAttribute(graph, "myField");
		packageRefactoring.has(classBeta);
		classBeta.has(operationBetaAbc2);
		classBeta.has(attributeBetaMyField);
		
		// Arrange graph workflow
		final var start = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var updateReferences = new GraphNodeMicrostepUpdateReferences(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var doubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		final var renameConflictingMethod = new GraphNodeRemedyRenameConflictingSymbol(graph);
		final var chooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		start.initiates(addMethod);
		addMethod.precedes(updateReferences);
		addMethod.causes(doubleDefinition);
		addMethod.obsolesces(missingDefinition);
		updateReferences.precedes(removeMethod);
		updateReferences.obsolesces(missingDefinition);
		removeMethod.causes(missingDefinition);
		renameConflictingMethod.mitigates(doubleDefinition);
		chooseDifferentName.mitigates(doubleDefinition);
		doubleDefinition.affects(operationAlphaAbc);
		doubleDefinition.affects(operationBetaAbc2);
		
		// Arrange graphics
		final var width = 2048;
		final var height = 1536;
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
