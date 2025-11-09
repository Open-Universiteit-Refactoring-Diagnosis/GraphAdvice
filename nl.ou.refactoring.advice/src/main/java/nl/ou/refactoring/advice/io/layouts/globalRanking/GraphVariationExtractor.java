package nl.ou.refactoring.advice.io.layouts.globalRanking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;

/**
 * Extracts graph variations from a graph.
 */
public final class GraphVariationExtractor {
	private GraphVariationExtractor() { }

	/**
	 * Extracts the variations from the specified graph.
	 * @param graph The graph to extract variations from.
	 * @param maximumDepth The maximum depth of a variation.
	 * @return The set of variations found in the graph.
	 * @throws ArgumentNullException Thrown if graph is null.
	 * @throws GraphPathSegmentInvalidException Thrown if a graph path segment is invalid.
	 */
	public static Set<GraphVariation> extract(Graph graph, int maximumDepth)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		ArgumentGuard.requireNotNull(graph, "graph");
		final Map<String, Integer> sequenceCounts = new HashMap<>();
		for (final var variationStartNode : graph.getNodes()) {
			var paths = variationStartNode.findPaths(maximumDepth);
			for (var path : paths) {
				var sequence = encodeSequence(path);
				sequenceCounts.merge(sequence, 1, Integer::sum);
			}
		}
		
		return
				sequenceCounts
					.entrySet()
					.stream()
					.map(entry -> {
						try {
							final var segments = decodeSequence(entry.getKey(), graph);
							return new GraphVariation(segments, entry.getValue());
						} catch (ClassNotFoundException ex) {
							ex.printStackTrace();
							return new GraphVariation(new ArrayList<>(), 0);
						}
					})
					.collect(Collectors.toUnmodifiableSet());
	}
	
	private static String getClassName(Class<? extends GraphNode> nodeClass) {
		if (GraphNodeMicrostep.class.isAssignableFrom(nodeClass)) {
			nodeClass = GraphNodeMicrostep.class;
		}
		
		return nodeClass.getName();
	}
	
	private static String encodeSequence(GraphPath path) {
		final var segments = path.getSegments();
		var sequence = getClassName(segments.get(0).getNode().getClass());
		for (var i = 1; i < segments.size(); i++) {
			final var segment = segments.get(i);
			final var nodeClassName = getClassName(segment.getNode().getClass());
			sequence += ",";
			sequence += segment.getEdge().getClass().getName();
			sequence += ",";
			sequence += nodeClassName;
		}
		return sequence;
	}
	
	private static List<GraphVariationSegment> decodeSequence(String encodedSequence, Graph graph)
			throws ClassNotFoundException {
		final var segments = new ArrayList<GraphVariationSegment>();
		final var parts = Arrays.asList(encodedSequence.split(","));
		final var nodeClassFirst = Class.forName(parts.get(0));
		segments.add(new GraphVariationSegment(null, (Class<? extends GraphNode>)nodeClassFirst));
		for (var i = 1; i < parts.size(); i += 2) {
			final var edgeClassName = parts.get(i);
			final var edgeClass = (Class<? extends GraphEdge>)Class.forName(edgeClassName);
			final var nodeClassName = parts.get(i + 1);
			final var nodeClass = (Class<? extends GraphNode>)Class.forName(nodeClassName);
			segments.add(new GraphVariationSegment(edgeClass, nodeClass));
		}
		return segments;
	}
}
