package nl.ou.refactoring.advice.io.images;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeInitiates;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgePrecedes;
import nl.ou.refactoring.advice.io.layouts.forceDirected.GraphLayoutForceDirectedSettings;
import nl.ou.refactoring.advice.io.layouts.globalRanking.GraphLayoutGlobalRankingSettings;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyRenameConflictingSymbol;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class GraphPainterTests {
	private static Path OUTPUT_DIR;
	
	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output", "images");
		Files.createDirectories(OUTPUT_DIR);
	}
	
	@Test
	@DisplayName("Should draw a Refactoring Advice Graph on an image using Force-Directed Layout")
	public void drawForceDirectedLayoutTest()
			throws
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				RefactoringMayContainOnlyOneStartNodeException {
		// Arrange graph
		final var graph = new Graph("Move Method");
		
		// Arrange graph code
		final var packageNodeRefactoring = GraphNodePackage.parse(graph, "ou.refactoring");
		final var identifierNodeAlpha = new GraphNodeIdentifier(graph, "Alpha");
		final var classNodeAlpha = new GraphNodeClass(graph, identifierNodeAlpha);
		final var identifierAbc = new GraphNodeIdentifier(graph, "abc");
		final var operationNodeAlphaAbc = new GraphNodeOperation(graph, identifierAbc);
		final var attributeNodeAlphaFoo = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "foo"));
		final var attributeNodeAlphaBar = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "bar"));
		packageNodeRefactoring.has(classNodeAlpha);
		classNodeAlpha.has(operationNodeAlphaAbc);
		classNodeAlpha.has(attributeNodeAlphaFoo);
		classNodeAlpha.has(attributeNodeAlphaBar);
		final var betaIdentifierNode = new GraphNodeIdentifier(graph, "Beta");
		final var classNodeBeta = new GraphNodeClass(graph, betaIdentifierNode);
		final var identifierNodeAbc2 = new GraphNodeIdentifier(graph, "abc2");
		final var operationNodeBetaAbc2 = new GraphNodeOperation(graph, identifierNodeAbc2);
		final var attributeNodeBetaMyField = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "myField"));
		packageNodeRefactoring.has(classNodeBeta);
		classNodeBeta.has(operationNodeBetaAbc2);
		classNodeBeta.has(attributeNodeBetaMyField);
		
		// Arrange graph workflow
		final var startNode = graph.start();
		final var microstepNodeAddMethod = new GraphNodeMicrostepAddMethod(graph);
		final var microstepNodeAddExpression = new GraphNodeMicrostepAddExpression(graph);
		final var microstepNodeRemoveExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var microstepNodeRemoveMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var riskNodeDoubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		final var riskNodeMissingDefinition = new GraphNodeRiskMissingDefinition(graph);
		final var remedyNodeRenameConflictingMethod = new GraphNodeRemedyRenameConflictingSymbol(graph);
		final var remedyNodeChooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		startNode.initiates(microstepNodeAddMethod);
		microstepNodeAddMethod.precedes(microstepNodeAddExpression);
		microstepNodeAddMethod.causes(riskNodeDoubleDefinition);
		microstepNodeAddMethod.obsolesces(riskNodeMissingDefinition);
		microstepNodeAddExpression.precedes(microstepNodeRemoveExpression);
		microstepNodeAddExpression.obsolesces(riskNodeMissingDefinition);
		microstepNodeRemoveExpression.precedes(microstepNodeRemoveMethod);
		microstepNodeRemoveMethod.causes(riskNodeMissingDefinition);
		remedyNodeRenameConflictingMethod.mitigates(riskNodeDoubleDefinition);
		remedyNodeChooseDifferentName.mitigates(riskNodeDoubleDefinition);
		riskNodeDoubleDefinition.affects(operationNodeAlphaAbc);
		riskNodeDoubleDefinition.affects(operationNodeBetaAbc2);
		
		// Arrange graphics
		final var width = 2048;
		final var height = 1536;
		final var graphPainter = new GraphPainter(width, height);
		final var graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final var graphics = graphImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Arrange layout
		final var layoutSettings = new GraphLayoutForceDirectedSettings();
		layoutSettings.setIterations(500000);
		layoutSettings.setSpringLength(250.0);
		
		// Act
		graphPainter.draw(graph, graphics, layoutSettings);
		
		// Assert
		assertEquals(width, graphImage.getWidth());
		assertEquals(height, graphImage.getHeight());
		// compare to reference image
		
		final var forceDirectedLayoutImageFilePath =
				OUTPUT_DIR.resolve("Graph_ForceDirectedLayout.png");
		try {
			ImageIO.write(graphImage, "png", forceDirectedLayoutImageFilePath.toFile());
		} catch (IOException exception) {
			fail("Failed to write test image");
		}
	}
	
	// @Test
	@DisplayName("Should draw a Refactoring Advice Graph on an image using Global Ranking Layout")
	public void drawGlobalRankingLayoutTest()
			throws
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				RefactoringMayContainOnlyOneStartNodeException {
		// Arrange graph
		final var graph = new Graph("Move Method");
		
		// Arrange graph code
		final var packageNodeRefactoring = GraphNodePackage.parse(graph, "ou.refactoring");
		final var alphaNodeIdentifierNode = new GraphNodeIdentifier(graph, "Alpha");
		final var classNodeAlpha = new GraphNodeClass(graph, alphaNodeIdentifierNode);
		final var identifierNodeAbc = new GraphNodeIdentifier(graph, "abc");
		final var operationNodeAlphaAbc = new GraphNodeOperation(graph, identifierNodeAbc);
		final var attributeNodeAlphaFoo = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "foo"));
		final var attributeNodeAlphaBar = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "bar"));
		packageNodeRefactoring.has(classNodeAlpha);
		classNodeAlpha.has(operationNodeAlphaAbc);
		classNodeAlpha.has(attributeNodeAlphaFoo);
		classNodeAlpha.has(attributeNodeAlphaBar);
		final var betaIdentifierNode = new GraphNodeIdentifier(graph, "Beta");
		final var classNodeBeta = new GraphNodeClass(graph, betaIdentifierNode);
		final var identifierNodeAbc2 = new GraphNodeIdentifier(graph, "abc2");
		final var operationNodeBetaAbc2 = new GraphNodeOperation(graph, identifierNodeAbc2);
		final var attributeNodeBetaMyField = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "myField"));
		packageNodeRefactoring.has(classNodeBeta);
		classNodeBeta.has(operationNodeBetaAbc2);
		classNodeBeta.has(attributeNodeBetaMyField);
		
		// Arrange graph workflow
		final var startNode = graph.start();
		final var microstepNodeAddMethod = new GraphNodeMicrostepAddMethod(graph);
		final var microstepNodeAddExpression = new GraphNodeMicrostepAddExpression(graph);
		final var microstepNodeRemoveExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var microstepNodeRemoveMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var riskNodeDoubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		final var riskNodeMissingDefinition = new GraphNodeRiskMissingDefinition(graph);
		final var remedyNodeRenameConflictingMethod = new GraphNodeRemedyRenameConflictingSymbol(graph);
		final var remedyNodeChooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		startNode.initiates(microstepNodeAddMethod);
		microstepNodeAddMethod.precedes(microstepNodeAddExpression);
		microstepNodeAddMethod.causes(riskNodeDoubleDefinition);
		microstepNodeAddMethod.obsolesces(riskNodeMissingDefinition);
		microstepNodeAddExpression.precedes(microstepNodeRemoveExpression);
		microstepNodeAddExpression.obsolesces(riskNodeMissingDefinition);
		microstepNodeRemoveExpression.precedes(microstepNodeRemoveMethod);
		microstepNodeRemoveExpression.obsolesces(riskNodeMissingDefinition);
		microstepNodeRemoveMethod.causes(riskNodeMissingDefinition);
		remedyNodeRenameConflictingMethod.mitigates(riskNodeDoubleDefinition);
		remedyNodeChooseDifferentName.mitigates(riskNodeDoubleDefinition);
		riskNodeDoubleDefinition.affects(operationNodeAlphaAbc);
		riskNodeDoubleDefinition.affects(operationNodeBetaAbc2);
		
		// Arrange graphics
		final var width = 2048;
		final var height = 1536;
		final var graphPainter = new GraphPainter(width, height);
		final var graphImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final var graphics = graphImage.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Arrange layout
		final var layoutSettings = new GraphLayoutGlobalRankingSettings();
		Map<Class<? extends GraphEdge>, Double> edgeWeights = new HashMap<>();
		edgeWeights.put(GraphEdgeInitiates.class, 10.0);
		edgeWeights.put(GraphEdgePrecedes.class, 10.0);
		layoutSettings.setEdgeWeights(edgeWeights);
		layoutSettings.setVariationMaximumDepth(10);
		
		// Act
		graphPainter.draw(graph, graphics, layoutSettings);
		
		// Assert
		assertEquals(width, graphImage.getWidth());
		assertEquals(height, graphImage.getHeight());
		// compare to reference image
		
		final var globalRankingLayoutImageFilePath =
				OUTPUT_DIR.resolve("Graph_GlobalRankingLayout.png");
		try {
			ImageIO.write(graphImage, "png", globalRankingLayoutImageFilePath.toFile());
		} catch (IOException exception) {
			fail("Failed to write test image");
		}
	}
}
