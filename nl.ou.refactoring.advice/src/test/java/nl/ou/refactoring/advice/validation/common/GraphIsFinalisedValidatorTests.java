package nl.ou.refactoring.advice.validation.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.validation.GraphValidationEngine;

public final class GraphIsFinalisedValidatorTests {
	@DisplayName("Graph is finalised validation")
	@ParameterizedTest
	@MethodSource("validateTestParameters")
	public void validateTest(Graph graph, boolean isValidExpected) {
		final var engine = new GraphValidationEngine();
		engine.addValidator(GraphIsFinalisedValidator.INSTANCE);
		final var results = engine.validate(graph);
		assertFalse(results.isEmpty());
		assertEquals(results.size(), 1);
		final var result = results.stream().findAny().get();
		assertInstanceOf(GraphIsFinalisedValidationResult.class, result);
		assertEquals(isValidExpected, result.getIsValid());
	}
	
	public static Stream<Arguments> validateTestParameters() {
		final var argumentsList = new ArrayList<Arguments>();
		
		final var graphNotFinalised = new Graph("Graph not finalised");
		argumentsList.add(Arguments.of(graphNotFinalised, false));
		
		final var graphFinalised = new Graph("Graph finalised");
		final var graphFinalisedStartNode = graphFinalised.start();
		final var graphAddMethodNode = new GraphNodeMicrostepAddMethod(graphFinalised);
		graphFinalisedStartNode.initiates(graphAddMethodNode);
		graphAddMethodNode.finalises();
		argumentsList.add(Arguments.of(graphFinalised, true));
		
		return argumentsList.stream();
	}
}