package nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;

public final class GraphNodeDangerTests {
	private static final Logger LOGGER = LogManager.getLogger(GraphNodeDangerTests.class);
	private static final Locale[] SUPPORTED_LOCALES = { Locale.of("nl", "NL"), Locale.of("en", "GB") };

	@ParameterizedTest
	@MethodSource("getAffectedTestCases")
	@DisplayName("Should get nodes affected by a danger")
	public void getAffectedTest(GraphNodeDanger risk, Set<GraphNodeBase> expected) {
		final var actual = risk.getAffected();
		assertEquals(expected, actual);
	}

	@ParameterizedTest
	@MethodSource("getCaptionsTestCases")
	@DisplayName("Should get localised captions for nodes")
	public void getCaptionsTest(Class<? extends GraphNodeDanger> riskNodeClass) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		final var graph = new Graph("Test");
		final var riskNode = (GraphNodeDanger) riskNodeClass.getConstructors()[0].newInstance(graph);

		for (final var locale : SUPPORTED_LOCALES) {
			Locale.setDefault(locale);
			final var result = riskNode.getCaption();
			assertNotNull(result);
			LOGGER.info(
					"getCaption (remedy: {}, locale: {}): {}",
					riskNode.getClass().getSimpleName(),
					locale.toLanguageTag(),
					result);
		}
	}

	private static Stream<Arguments> getAffectedTestCases() {
		final var argumentsList = new ArrayList<Arguments>();

		final var graphTest = new Graph("Affected Test");
		final var graphTestNodeRisk = new GraphNodeDangerDoubleDefinition(graphTest);
		final var test1Identifier = GraphNodeIdentifier.computeIdentifier(graphTest, "test1");
		final var graphTestNodeOperation1 = new GraphNodeOperation(graphTest, test1Identifier);
		final var test2Identifier = GraphNodeIdentifier.computeIdentifier(graphTest, "test2");
		final var graphTestNodeOperation2 = new GraphNodeOperation(graphTest, test2Identifier);
		graphTestNodeRisk.affects(graphTestNodeOperation1);
		graphTestNodeRisk.affects(graphTestNodeOperation2);
		argumentsList.add(Arguments.of(graphTestNodeRisk, Set.of(graphTestNodeOperation1, graphTestNodeOperation2)));

		return argumentsList.stream();
	}

	private static Stream<Arguments> getCaptionsTestCases() {
		final String packageName = "nl.ou.refactoring.advice.nodes.diagnosisresolutions.dangers";
		try (final var scanResult =
				new ClassGraph()
						// .verbose()
						.enableAllInfo()
						.acceptPackages(packageName)
						.scan()) {
			// Collect results into a list while ScanResult is open
			List<Arguments> resultList = new ArrayList<>();
			for (ClassInfo classInfo : scanResult.getSubclasses(GraphNodeDanger.class)) {
				try {
					resultList.add(Arguments.of(classInfo.loadClass()));
				} catch (Exception e) {
					// Add null for classes that could not be loaded.
					LOGGER.catching(e);
					LOGGER.warn("Could not load class '{}'", classInfo.getName());
					resultList.add(Arguments.of((Class<?>) null));
				}
			}
			return resultList.stream();
		}
	}
}
