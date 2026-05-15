package nl.ou.refactoring.advice.nodes.workflow.risks.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;
import nl.ou.refactoring.advice.validation.GraphValidationEngine;

public final class GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidatorTests {
	@DisplayName("Graph Double Definition present when required validation")
	@ParameterizedTest
	@MethodSource("validateTestParameters")
	public void validateTest(Graph graph, boolean isValidExpected) {
		final var engine = new GraphValidationEngine();
		engine.addValidator(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator.INSTANCE);
		final var results = engine.validate(graph);
		assertFalse(results.isEmpty());
		assertEquals(results.size(), 1);
		final var result = results.stream().findAny().get();
		assertInstanceOf(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult.class, result);
		assertEquals(isValidExpected, result.getIsValid());
	}
	
	public static Stream<Arguments> validateTestParameters() {
		return
			Stream.of(
				Arguments.of(createGraphMissingRiskDoubleDefinitionMethod(), false),
				Arguments.of(createGraphNotMissingRiskDoubleDefinitionMethod(), true)
			);
	}
	
	private static Graph createGraphMissingRiskDoubleDefinitionMethod() {
		final var graph = new Graph("Graph missing risk Double Definition with operation/method");
		final var graphStart = graph.start();
		final var addMethodNode = new GraphNodeMicrostepAddMethod(graph);
		graphStart.initiates(addMethodNode);
		addMethodNode.finalises();
		
		final var packageNode = new GraphNodePackage(graph, new GraphNodeIdentifier(graph, "test"));
		final var classNode = new GraphNodeClass(graph, new GraphNodeIdentifier(graph, "ClassA"));
		final var operationNodeExisting = new GraphNodeOperation(graph, new GraphNodeIdentifier(graph, "foo"));
		final var operationNodeNew = new GraphNodeOperation(graph, new GraphNodeIdentifier(graph, "foo"));
		packageNode.has(classNode);
		classNode.has(operationNodeExisting);
		classNode.has(operationNodeNew);
		addMethodNode.adds(operationNodeNew);
		
		return graph;
	}
	
	private static Graph createGraphNotMissingRiskDoubleDefinitionMethod() {
		final var graph = createGraphMissingRiskDoubleDefinitionMethod();
		
		final var riskDoubleDefinitionNode = new GraphNodeRiskDoubleDefinition(graph);
		final var addMethodNode =
			graph
				.getNodes(GraphNodeMicrostepAddMethod.class)
				.stream()
				.findAny()
				.get();
		final var operationNodeNew = addMethodNode.getOperationNode().get();
		addMethodNode.causes(riskDoubleDefinitionNode);
		riskDoubleDefinitionNode.affects(operationNodeNew);
		
		return graph;
	}
	
	@DisplayName("Graph Double Definition present when required validation fix")
	@ParameterizedTest
	@MethodSource("fixTestParameters")
	public void fixTest(Graph graph) {
		final var engine = new GraphValidationEngine();
		engine.addValidator(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidator.INSTANCE);
		final var results = engine.validate(graph);
		assertFalse(results.isEmpty());
		assertEquals(results.size(), 1);
		final var result = results.stream().findAny().get();
		assertInstanceOf(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult.class, result);
		
		// Validate risk is actually absent.
		assertTrue(graph.getNodes(GraphNodeRiskDoubleDefinition.class).isEmpty());
		
		final var doubleDefinitionValidationResult =
			(GraphNodeRiskDoubleDefinitionPresentWhenRequiredValidationResult)result;
		doubleDefinitionValidationResult.fix(false);
		
		// Validate risk is not absent anymore.
		final var risks = graph.getNodes(GraphNodeRiskDoubleDefinition.class);
		assertFalse(risks.isEmpty());
		
	}
	
	private static Stream<Arguments> fixTestParameters() {
		return
			Stream.of(
				Arguments.of(createGraphMissingRiskDoubleDefinitionMethod())	
			);
	}
}
