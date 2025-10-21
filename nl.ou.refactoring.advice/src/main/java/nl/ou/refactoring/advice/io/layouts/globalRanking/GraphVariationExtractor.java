package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;

public final class GraphVariationExtractor {
	private final Map<String, Integer> sequenceCounts = new HashMap<>();
	
	public GraphVariationExtractor() { }

	public Set<GraphVariation> extract(Graph graph, int maximumDepth) {
		for (final var variationStartNode : graph.getNodes()) {
			this.depthFirstSearch(variationStartNode, new ArrayList<>(), maximumDepth);
		}
		
		return
				this
					.sequenceCounts
					.entrySet()
					.stream()
					.map(entry -> {
						final var edges = decodeSequence(entry.getKey(), graph);
						return new GraphVariation(edges, entry.getValue());
					})
					.collect(Collectors.toSet());
	}
	
	private void depthFirstSearch(
			GraphNode current,
			List<GraphEdge> path,
			int remainingDepth) {
		if (remainingDepth == 0) {
			return;
		}
		
		for (final var edge : current.getEdges()) {
			List<GraphEdge> pathNew = new ArrayList<>(path);
			pathNew.add(edge);
			var key = encodeSequence(pathNew);
			this.sequenceCounts.merge(key, 1, Integer::sum);
			this.depthFirstSearch(edge.getDestinationNode(), pathNew, remainingDepth - 1);
		}
	}
	
	private static String encodeSequence(List<GraphEdge> edges) {
		return
				edges
					.stream()
					.map(edge -> edge.getId().toString())
					.collect(Collectors.joining(","));
	}
	
	private static List<GraphEdge> decodeSequence(String encodedSequence, Graph graph) {
		final var edgeIdentifiers = Arrays.asList(encodedSequence.split(","));
		return
				edgeIdentifiers
					.stream()
					.map(identifier -> graph.getEdge(UUID.fromString(identifier)))
					.toList();
	}
}
