package nl.ou.refactoring.advice.nodes.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

final class GraphWorkflowExplorerTests {
	@ParameterizedTest
	@ArgumentsSource(GraphWorkflowExplorerGetMicrostepsTestArgumentsProvider.class)
	@DisplayName("Should provide the microsteps in a Refactoring Advice Graph (RAG)")
	public void getMicrostepsTest(Graph graph, Set<GraphNodeMicrostep> expected) {
		final var actual = GraphWorkflowExplorer.getMicrosteps(graph);
		assertEquals(expected, actual);
	}
	
	@ParameterizedTest
	@ArgumentsSource(GraphWorkflowExplorerGetWorkflowTestArgumentsProvider.class)
	@DisplayName("Should provide the workflow in a Refactoring Advice Graph (RAG)")
	public void getWorkflowTest(Graph graph, GraphPath expected) {
		final var actual = GraphWorkflowExplorer.getWorkflow(graph, 10);
		assertEquals(expected, actual);
	}
	
	@ParameterizedTest
	@ArgumentsSource(GraphWorkflowExplorerGetRisksTestArgumentsProvider.class)
	@DisplayName("Should provide the risks in a Refactoring Advice Graph (RAG)")
	public void getRisksTest(Graph graph, Set<GraphNodeRisk> expected) {
		final var actual = GraphWorkflowExplorer.getRisks(graph);
		assertEquals(expected, actual);
	}
}