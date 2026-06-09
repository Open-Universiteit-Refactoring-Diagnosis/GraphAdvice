package nl.ou.refactoring.advice.io.plantuml.classDiagrams;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.GraphNodeType;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassHasMultipleGeneralisationsException;
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

public final class GraphPlantUmlClassDiagramWriterTests {
	private static Path OUTPUT_DIR;
	
	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output", "PlantUML", "classDiagrams");
		Files.createDirectories(OUTPUT_DIR);
	}

	@Test
	@DisplayName("Should write a PlantUML class diagram from a graph")
	public void writeTests()
			throws
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				RefactoringMayContainOnlyOneStartNodeException,
				GraphNodeClassHasMultipleGeneralisationsException
	{
		// Arrange graph
		final var graph = new Graph("Move Method");
		
		// Arrange graph code
		final var packageNodeRefactoring = GraphNodePackage.parse(graph, "ou.refactoring");
		final var typeNodeInt = GraphNodeType.computeType(graph, "int");
		final var typeNodeString = GraphNodeType.computeType(graph, "String");
		final var identifierNodeAlpha = new GraphNodeIdentifier(graph, "Alpha");
		final var classNodeAlpha = new GraphNodeClass(graph, identifierNodeAlpha);
		final var attributeNodeAlphaFoo = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "foo"));
		final var attributeNodeAlphaBar = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "bar"));
		final var identifierNodeAbc = new GraphNodeIdentifier(graph, "abc");
		final var operationNodeAlphaAbc = new GraphNodeOperation(graph, identifierNodeAbc);
		packageNodeRefactoring.has(classNodeAlpha);
		classNodeAlpha.has(operationNodeAlphaAbc);
		classNodeAlpha.has(attributeNodeAlphaFoo);
		classNodeAlpha.has(attributeNodeAlphaBar);
		attributeNodeAlphaFoo.is(typeNodeString);
		attributeNodeAlphaBar.is(typeNodeInt);
		operationNodeAlphaAbc.hasReturnType(typeNodeString);
		final var identifierNodeBeta = new GraphNodeIdentifier(graph, "Beta");
		final var classNodeBeta = new GraphNodeClass(graph, identifierNodeBeta);
		final var attributeNodeBetaMyField = new GraphNodeAttribute(graph, new GraphNodeIdentifier(graph, "myField"));
		final var identifierNodeAbc2 = new GraphNodeIdentifier(graph, "abc2");
		final var operationNodeBetaAbc2 = new GraphNodeOperation(graph, identifierNodeAbc2);
		packageNodeRefactoring.has(classNodeBeta);
		classNodeBeta.has(operationNodeBetaAbc2);
		classNodeBeta.has(attributeNodeBetaMyField);
		attributeNodeBetaMyField.is(typeNodeString);
		operationNodeBetaAbc2.hasReturnType(typeNodeInt);
		
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
		microstepNodeRemoveMethod.finalises();
		remedyNodeRenameConflictingMethod.mitigates(riskNodeDoubleDefinition);
		remedyNodeChooseDifferentName.mitigates(riskNodeDoubleDefinition);
		riskNodeDoubleDefinition.affects(operationNodeAlphaAbc);
		riskNodeDoubleDefinition.affects(operationNodeBetaAbc2);
		
		// Arrange writer
		final var stringWriter = new StringWriter();
		final var classDiagramWriter = new GraphPlantUmlClassDiagramWriter(stringWriter);
		
		// Act / Assert
		classDiagramWriter.write(graph);
		final var plantUmlClassDiagramFilePath =
				OUTPUT_DIR.resolve("Graph_PlantUMLClassDiagram.puml");
		try (final var bufferedWriter =
				new BufferedWriter(new FileWriter(plantUmlClassDiagramFilePath.toFile()))) {
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
		} catch (IOException exception) {
			fail("Failed to write PlantUML Class Diagram");
		}
	}
}
