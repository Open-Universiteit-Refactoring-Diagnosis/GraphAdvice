package nl.ou.refactoring.advice.nodes.workflow.microsteps;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

public final class GraphNodeMicrostepTests {
	private static final Logger LOGGER = LogManager.getLogger(GraphNodeMicrostepTests.class);
	private static final Locale[] SUPPORTED_LOCALES = {
			Locale.of("nl", "NL"),
			Locale.of("en", "GB")
		};

	@ParameterizedTest
	@MethodSource("getCaptionsTestCases")
	@DisplayName("Should get localised captions for nodes")
	public void getCaptionsTest(
			Class<? extends GraphNodeMicrostep> microstepNodeClass
	)
			throws
				InstantiationException,
				IllegalAccessException,
				IllegalArgumentException,
				InvocationTargetException,
				NoSuchMethodException
	{
		final var graph = new Graph("Test");
		final var microstepNode = (GraphNodeMicrostep)microstepNodeClass.getConstructors()[0].newInstance(graph);
		
		for (final var locale : SUPPORTED_LOCALES) {
			Locale.setDefault(locale);
			final var result = microstepNode.getCaption();
			assertNotNull(result);
			LOGGER.info(
					"getCaption (microstep: {}, locale: {}): {}",
					microstepNode.getClass().getSimpleName(),
					locale.toLanguageTag(),
					result
			);
		}
	}
	
	private static Stream<Arguments> getCaptionsTestCases() {
	    final String packageName = "nl.ou.refactoring.advice.nodes.workflow.microsteps";
	    try (final var scanResult = new ClassGraph()
	            //.verbose()
	            .enableAllInfo()
	            .acceptPackages(packageName)
	            .scan()) {
	        // Collect results into a list while ScanResult is open
	        List<Arguments> resultList = new ArrayList<>();
	        for (ClassInfo classInfo : scanResult.getSubclasses(GraphNodeMicrostep.class)) {
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
