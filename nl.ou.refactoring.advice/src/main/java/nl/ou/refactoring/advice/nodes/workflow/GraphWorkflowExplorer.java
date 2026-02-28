package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * Explores the workflow of Refactoring Advice Graphs (RAGs).
 */
public final class GraphWorkflowExplorer {

	private GraphWorkflowExplorer() { }
	
	/**
	 * Finds all microsteps in the advice graph.
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of microsteps in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeMicrostep> getMicrosteps(Graph graph)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return
			graph
				.getNodes(GraphNodeMicrostep.class);
	}

	/**
	 * Finds all risks in the advice graph.
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of risks in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeRisk> getRisks(Graph graph)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return
			graph
				.getNodes(GraphNodeRisk.class);
	}
	
	/**
	 * Finds all dangers in the advice graph.
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of dangers in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeRisk> getDangers(Graph graph)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return
			getRisks(graph)
				.stream()
				.filter(node -> node.getNeutralisers().size() == 0)
				.collect(Collectors.toSet());
	}
}
