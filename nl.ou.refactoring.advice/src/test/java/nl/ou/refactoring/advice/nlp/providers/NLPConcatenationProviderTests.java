package nl.ou.refactoring.advice.nlp.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nlp.processors.NLPConcatenationProcessor;
import nl.ou.refactoring.advice.nodes.GraphNode;

public final class NLPConcatenationProviderTests {
	@DisplayName("Should produce natural language text as expected")
	@ParameterizedTest
	@ArgumentsSource(NLPConcatenationProviderTestArgumentsProvider.class)
	public void processTests(Graph graph, String textExpected, Map<String, GraphNode> referencesExpected) {
		final var nlpConcatenationProvider = new NLPConcatenationProcessor();
		final var nlpResult = nlpConcatenationProvider.process(graph);
		final var textActual = nlpResult.getText();
		final var referencesActual = nlpResult.getReferences();
		assertEquals(textExpected, textActual);
		assertEquals(referencesExpected, referencesActual);
	}
}