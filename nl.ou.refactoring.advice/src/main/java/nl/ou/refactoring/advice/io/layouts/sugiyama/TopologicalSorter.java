package nl.ou.refactoring.advice.io.layouts.sugiyama;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Sorts graph nodes according to their topology using Depth First Search.
 */
public final class TopologicalSorter {
	/**
	 * Initialises a new instance of {@link TopologicalSorter}.
	 */
	private TopologicalSorter() { }

	/**
	 * Sorts the nodes in the specified graph in topological order, using Depth First Search.
	 * @param graph The graph of which to sort the nodes in topological order.
	 * @return A topologically sorted list of nodes from the graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	public static LinkedList<GraphNode> sort(Graph graph) {
		ArgumentGuard.requireNotNull(graph, "graph");
		final Set<GraphNode> visited = new HashSet<>();
		final LinkedList<GraphNode> sorted = new LinkedList<>();
		for (var node : graph.getNodes()) {
			if (!visited.contains(node)) {
				depthFirstSearch(node, visited, sorted);
			}
		}
		return sorted;
	}
	
	private static void depthFirstSearch(
			GraphNode node,
			Set<GraphNode> visited,
			LinkedList<GraphNode> sorted) {
		visited.add(node);
		final var neighbours =
				node
					.getEdges()
					.stream()
					.map(edge -> edge.getDestinationNode())
					.collect(Collectors.toSet());
		for (var neighbour : neighbours) {
			if (!visited.contains(neighbour)) {
				depthFirstSearch(neighbour, visited, sorted);
			}
		}
		sorted.addFirst(node);
	}
}
