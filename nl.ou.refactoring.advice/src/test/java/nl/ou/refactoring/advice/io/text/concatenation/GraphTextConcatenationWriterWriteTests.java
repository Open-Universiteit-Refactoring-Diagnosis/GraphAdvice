package nl.ou.refactoring.advice.io.text.concatenation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.GraphNodeClassStereotype;
import nl.ou.refactoring.advice.nodes.code.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemoveMethod;

public class GraphTextConcatenationWriterWriteTests {
	private static final Logger LOGGER =
			LogManager.getLogger(GraphTextConcatenationWriterWriteTests.class);
	private static Path OUTPUT_DIR;
	
	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output");
		Files.createDirectories(OUTPUT_DIR);
	}
	
	@ParameterizedTest
	@MethodSource("writeTestCases")
	@DisplayName("Should generate a refactoring advice text from a Refactoring Advice Graph")
	public void writeTest(Graph graph, String expected) {
		final var stringWriter = new StringWriter();
		final var concatenationWriter = new GraphTextConcatenationWriter(stringWriter);
		concatenationWriter.write(graph);
		assertEquals(expected, stringWriter.toString());
	}
	
	private static Stream<Arguments> writeTestCases() {
		final List<Arguments> arguments = new ArrayList<Arguments>();
		
		final var graphStartOnly = createGraphStartOnly();
		LOGGER.info("Testing graph '{}'", graphStartOnly.getRefactoringName());
		final var textStartOnly = "";
		arguments.add(Arguments.of(graphStartOnly, textStartOnly));
		
		final var graphMoveMethod = createGraphMoveMethod();
		LOGGER.info("Testing graph '{}'", graphMoveMethod.getRefactoringName());
		final var textMoveMethod = "";
		arguments.add(Arguments.of(graphMoveMethod, textMoveMethod));
		
		return arguments.stream();
	}
	
	private static Graph createGraphStartOnly() {
		final var graph = new Graph("Start only");
		graph.start();
		return graph;
	}
	
	private static Graph createGraphMoveMethod() {
		final var graph = new Graph("Move method");
		
		final var packageTest = new GraphNodePackage(graph, "nl.ou.refactoring.test");
		final var classAlphaBefore =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.BEFORE
				);
		packageTest.has(classAlphaBefore);
		final var operationBefore =
				new GraphNodeOperation(
						graph,
						"helloWorld"
				);
		classAlphaBefore.has(operationBefore);
		final var classBetaBefore =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.BEFORE
				);
		packageTest.has(classBetaBefore);
		final var classAlphaAfter =
				new GraphNodeClass(
						graph,
						"Alpha",
						GraphNodeClassStereotype.AFTER
				);
		packageTest.has(classAlphaAfter);
		final var classBetaAfter =
				new GraphNodeClass(
						graph,
						"Beta",
						GraphNodeClassStereotype.AFTER
				);
		packageTest.has(classBetaAfter);
		final var operationAfter =
				new GraphNodeOperation(
						graph,
						"helloWorld"
				);
		classBetaAfter.has(operationAfter);
		
		final var startNode = graph.start();
		final var addMethod = new GraphNodeMicrostepAddMethod(graph);
		startNode.initiates(addMethod);
		addMethod.adds(operationAfter);
		final var removeMethod = new GraphNodeMicrostepRemoveMethod(graph);
		addMethod.precedes(removeMethod);
		removeMethod.removes(operationBefore);
		removeMethod.finalises();
		
		return graph;
	}
}
