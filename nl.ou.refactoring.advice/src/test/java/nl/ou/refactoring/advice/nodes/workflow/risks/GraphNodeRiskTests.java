package nl.ou.refactoring.advice.nodes.workflow.risks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveExpression;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

public final class GraphNodeRiskTests {
	private static final Logger LOGGER = LogManager.getLogger(GraphNodeRiskTests.class);
	private static final Locale[] SUPPORTED_LOCALES = {
		Locale.of("nl", "NL"),
		Locale.of("en", "GB")
	};
	
	@ParameterizedTest
	@MethodSource("getCaptionsTestCases")
	@DisplayName("Should get localised captions for nodes")
	public void getCaptionsTest(
			Class<? extends GraphNodeRisk> riskNodeClass
	)
			throws
				InstantiationException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException
	{
		final var graph = new Graph("Test");
		final var riskNode = (GraphNodeRisk)riskNodeClass.getConstructors()[0].newInstance(graph);
		
		for (final var locale : SUPPORTED_LOCALES) {
			Locale.setDefault(locale);
			final var result = riskNode.getCaption();
			assertNotNull(result);
			LOGGER.info(
					"getCaption (remedy: {}, locale: {}): {}",
					riskNode.getClass().getSimpleName(),
					locale.toLanguageTag(),
					result
			);
		}
	}
	
	private static Stream<Arguments> getCaptionsTestCases() {
	    final String packageName = "nl.ou.refactoring.advice.nodes.workflow.risks";
	    try (final var scanResult = new ClassGraph()
	            //.verbose()
	            .enableAllInfo()
	            .acceptPackages(packageName)
	            .scan()) {
	        // Collect results into a list while ScanResult is open
	        List<Arguments> resultList = new ArrayList<>();
	        for (ClassInfo classInfo : scanResult.getSubclasses(GraphNodeRisk.class)) {
	            try {
	                resultList.add(Arguments.of(classInfo.loadClass()));
	            } catch (Exception e) {
	                // Add null for unloadable classes
	                resultList.add(Arguments.of((Class<?>) null));
	            }
	        }
	        return resultList.stream();
	    }
	}
	
	@Test
	@DisplayName("Should get microsteps that neutralise the risk")
	public void getNeutralisersTest() {
		// Arrange
		Graph graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		addMethod.obsolesces(missingDefinition);
		addMethod.precedes(addExpression);
		addExpression.obsolesces(missingDefinition);
		addExpression.precedes(removeExpression);
		removeExpression.precedes(removeMethod);
		removeMethod.causes(missingDefinition);
		
		// Act
		final var neutralisers = missingDefinition.getNeutralisers();
		
		// Assert
		assertTrue(neutralisers.contains(addMethod));
		assertTrue(neutralisers.contains(addExpression));
	}
	
	@Test
	@DisplayName("Should get microsteps that neutralise the risk, but omit microsteps that are not in a chain")
	public void getNeutralisersInvalidChainOfMicrostepsTest() {
		// Arrange
		Graph graph = new Graph("Refactoring test");
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		final var addExpression = new GraphNodeMicrostepAddExpression(graph);
		final var removeExpression = new GraphNodeMicrostepRemoveExpression(graph);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		final var missingDefinition = new GraphNodeRiskMissingDefinition(graph);
		addMethod.obsolesces(missingDefinition);
		addExpression.obsolesces(missingDefinition);
		addExpression.precedes(removeExpression);
		removeExpression.precedes(removeMethod);
		removeMethod.causes(missingDefinition);
		
		// Act
		final var neutralisers = missingDefinition.getNeutralisers();
		
		// Assert
		assertEquals(0, neutralisers.size());
	}
}
