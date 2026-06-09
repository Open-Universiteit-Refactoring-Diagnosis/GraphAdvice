package nl.ou.refactoring.advice.nlp.providers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.ParameterDeclarations;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphTemplates;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReader;
import nl.ou.refactoring.advice.io.javaParser.GraphJavaReaderResolutionProvider;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.operations.GraphNodeOperation;
import nl.ou.refactoring.advice.nodes.code.tokens.GraphNodeIdentifier;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAddMethod;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRiskDoubleDefinition;

public final class NLPConcatenationProviderTestArgumentsProvider implements ArgumentsProvider {
	@Override
	public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context)
			throws Exception {
		final var argumentsList = new ArrayList<Arguments>();
		argumentsList.add(createGraphMoveMethodDoubleDefinition());
		return argumentsList.stream();
	}

	private static Arguments createGraphMoveMethodDoubleDefinition() {
		final var graph = GraphTemplates.moveMethod().clone("Move Method causes Double Definition");
		final var resolutionProvider = new GraphJavaReaderResolutionProvider() {
			@Override
			public <T extends GraphNodeCode> Optional<T> resolveByFullyQualifiedName(
					Graph graph,
					String fullyQualifiedName,
					Class<T> resultType) {
				return Optional.empty();
			}
		};

		// Microsteps
		final var microstepAddMethod = graph.getNodes(GraphNodeMicrostepAddMethod.class).stream().findAny().get();

		// Alpha
		final var classAlphaJavaString =
				"package nl.ou.refactoring.moveMethod.doubleDefinition; class Alpha { private String foo() { return \"\"; } }";
		final var classAlphaJavaStringReader = new StringReader(classAlphaJavaString);
		var graphJavaReader =
				new GraphJavaReader(
						graph,
						resolutionProvider,
						classAlphaJavaStringReader,
						"nl/ou/refactoring/moveMethod/doubleDefinition/Alpha.java",
						"Alpha.java");
		graphJavaReader.read();

		// Beta
		final var classBetaJavaString =
				"package nl.ou.refactoring.moveMethod.doubleDefinition; class Beta { private String foo() { return \"\"; } }";
		final var classBetaJavaStringReader = new StringReader(classBetaJavaString);
		graphJavaReader =
				new GraphJavaReader(
						graph,
						resolutionProvider,
						classBetaJavaStringReader,
						"nl/ou/refactoring/moveMethod/doubleDefinition/Beta.java",
						"Beta.java");
		graphJavaReader.read();

		// Dangers
		final var fooIdentifier = new GraphNodeIdentifier(graph, "foo");
		final var classBeta =
				graph.getNode("nl.ou.refactoring.moveMethod.doubleDefinition.Beta", GraphNodeClass.class).get();
		final var classBetaFoo1OperationNode = classBeta.getOperationNode("foo", List.of()).get();
		final var classBetaFoo2OperationNode = new GraphNodeOperation(graph, fooIdentifier, List.of());
		microstepAddMethod.adds(classBetaFoo2OperationNode);
		final var doubleDefinitionNode = new GraphNodeRiskDoubleDefinition(graph);
		doubleDefinitionNode.affects(classBetaFoo1OperationNode);
		doubleDefinitionNode.affects(classBetaFoo2OperationNode);
		microstepAddMethod.causes(doubleDefinitionNode);

		final var methodOtherId = String.format("{%s}", classBetaFoo1OperationNode.getId().toString());
		final var methodAddedId = String.format("{%s}", classBetaFoo2OperationNode.getId().toString());
		final var references = new HashMap<String, GraphNode>();
		references.put(methodOtherId, classBetaFoo1OperationNode);
		references.put(methodAddedId, classBetaFoo2OperationNode);

		var firstId = methodAddedId.compareTo(methodOtherId) > 0 ? methodOtherId : methodAddedId;
		var secondId = methodAddedId.compareTo(methodOtherId) > 0 ? methodAddedId : methodOtherId;
		return Arguments.of(
				graph,
				String.format(
						"Move Method causes Double Definition, adds method '%s' which will introduce code symbols with identical signatures on '%s' and '%s'",
						methodAddedId,
						firstId,
						secondId),
				references);
	}
}