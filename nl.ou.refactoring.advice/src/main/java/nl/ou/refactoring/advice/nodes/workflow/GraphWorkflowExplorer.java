package nl.ou.refactoring.advice.nodes.workflow;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.workflow.GraphEdgeWorkflow;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.risks.GraphNodeRisk;

/**
 * Explores the workflow of Refactoring Advice Graphs (RAGs).
 */
public final class GraphWorkflowExplorer {
	/**
	 * Initialises a new instance of {@link GraphWorkflowExplorer}. This is a static
	 * class; do not call this constructor.
	 */
	private GraphWorkflowExplorer() {
	}

	/**
	 * Finds all microsteps in the advice graph.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of microsteps in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeMicrostep> getMicrosteps(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return graph.getNodes(GraphNodeMicrostep.class);
	}

	/**
	 * Gets the path from the start of the specified Refactoring Advice Graph's
	 * (RAG) workflow subgraph.
	 * 
	 * @param graph        The Refactoring Advice Graph (RAG).
	 * @param maximumDepth The maximum depth of the path. This is to avoid an
	 *                     infinitely long workflow.
	 * @return The path from the start of the specified Refactoring Advice Graph's
	 *         (RAG) workflow subgraph.
	 * @throws ArgumentNullException                    Thrown if graph is null.
	 * @throws IllegalArgumentException                 Thrown if maximumDepth is
	 *                                                  less than 0.
	 * @throws RefactoringMustContainStartNodeException Thrown if the graph does not
	 *                                                  contain a start node.
	 */
	public static GraphPath getWorkflow(Graph graph, int maximumDepth)
			throws ArgumentNullException, IllegalArgumentException, RefactoringMustContainStartNodeException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireGreaterThanOrEqual(0, maximumDepth, "maximumDepth");

		final var startNode = graph.getStart().orElseThrow(() -> new RefactoringMustContainStartNodeException());
		final var path = new GraphPath(startNode);
		Optional<GraphNodeWorkflow> currentNode = Optional.of(startNode);
		while (currentNode.isPresent()) {
			switch (currentNode.get()) {
				case GraphNodeWorkflow workflowNode: {
					final Optional<GraphEdgeWorkflow> workflowEdgeOptional =
							workflowNode.getEdges(GraphEdgeWorkflow.class).stream().findAny();
					if (workflowEdgeOptional.isPresent()) {
						final var workflowEdge = workflowEdgeOptional.get();
						final var nextNode = workflowEdge.getDestinationNode();
						path.append(workflowEdge, nextNode);
						currentNode = Optional.of(nextNode);
						break;
					}

					currentNode = Optional.empty();
					break;
				}
			}
		}
		return path;
	}

	/**
	 * Finds all risks in the advice graph.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of risks in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeRisk> getRisks(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return graph.getNodes(GraphNodeRisk.class);
	}

	/**
	 * Finds all dangers in the advice graph.
	 * 
	 * @param graph The Refactoring Advice Graph (RAG).
	 * @return A set of dangers in the advice graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static Set<GraphNodeRisk> getDangers(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return getRisks(graph).stream().filter(node -> node.getNeutralisers().size() == 0).collect(Collectors.toSet());
	}
}
