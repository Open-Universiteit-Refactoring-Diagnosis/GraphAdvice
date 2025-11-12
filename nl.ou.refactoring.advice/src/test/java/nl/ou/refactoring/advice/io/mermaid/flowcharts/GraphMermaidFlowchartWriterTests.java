package nl.ou.refactoring.advice.io.mermaid.flowcharts;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartDirection;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.RefactoringMayContainOnlyOneStartNodeException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyChooseDifferentName;
import nl.ou.refactoring.advice.nodes.workflow.remedies.GraphNodeRemedyRenameConflictingSymbol;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskMissingDefinition;

public final class GraphMermaidFlowchartWriterTests {

	public GraphMermaidFlowchartWriterTests() { }

	@Test
	@DisplayName("Should write a Mermaid flowchart from a graph")
	public void writeTests()
			throws ArgumentNullException, GraphPathSegmentInvalidException, RefactoringMayContainOnlyOneStartNodeException {
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
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var doubleDefinition = new GraphNodeRiskDoubleDefinition(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		final var renameConflictingMethod = new GraphNodeRemedyRenameConflictingSymbol(graph);
		final var chooseDifferentName = new GraphNodeRemedyChooseDifferentName(graph);
		start.initiates(addMethod);
		addMethod.precedes(addExpression);
		addMethod.causes(doubleDefinition);
		addMethod.obsolesces(missingDefinition);
		addExpression.precedes(removeExpression);
		addExpression.obsolesces(missingDefinition);
		removeExpression.precedes(removeMethod);
		removeMethod.causes(missingDefinition);
		removeMethod.finalises();
		renameConflictingMethod.mitigates(doubleDefinition);
		chooseDifferentName.mitigates(doubleDefinition);
		doubleDefinition.affects(operationAlphaAbc);
		doubleDefinition.affects(operationBetaAbc2);
		
		// Arrange writer
		final var stringWriter = new StringWriter();
		final var writer = new GraphMermaidFlowchartWriter(stringWriter, GraphMermaidFlowchartDirection.LeftToRight);
		
		// Act / Assert
		writer.write(graph);
		try {
			final var file = new File("C:\\Temp\\Graph_MermaidFlowchart.md");
			file.delete();
			file.createNewFile();
			var bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
		} catch (IOException exception) {
			fail("Failed to write Mermaid Flowchart");
		}
	}
}
