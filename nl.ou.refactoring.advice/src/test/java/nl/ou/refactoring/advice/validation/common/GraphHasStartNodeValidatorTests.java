package nl.ou.refactoring.advice.validation.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.validation.GraphValidationEngine;

public final class GraphHasStartNodeValidatorTests {
	@DisplayName("Graph has a start node validation")
	@ParameterizedTest
	@MethodSource("validateTestParameters")
	public void validateTest(Graph graph, boolean isValidExpected) {
		final var engine = new GraphValidationEngine();
		engine.addValidator(GraphHasStartNodeValidator.INSTANCE);
		final var results = engine.validate(graph);
		assertFalse(results.isEmpty());
		assertEquals(results.size(), 1);
		final var result = results.stream().findAny().get();
		assertInstanceOf(GraphHasStartNodeValidationResult.class, result);
		assertEquals(isValidExpected, result.getIsValid());
	}
	
	public static Stream<Arguments> validateTestParameters() {
		return
			Stream.of(
				Arguments.of(new Graph("Graph without a start node"), false),
				Arguments.of(new Graph("Graph with a start node").start().getGraph(), true)
			);
	}
}