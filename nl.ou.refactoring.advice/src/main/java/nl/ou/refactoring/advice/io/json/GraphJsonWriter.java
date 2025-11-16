package nl.ou.refactoring.advice.io.json;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriter;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Writes Refactoring Advice Graphs to JSON.
 */
public final class GraphJsonWriter implements GraphWriter {
	private final Writer writer;
	
	/**
	 * Initialises a new instance of {@link GraphJsonWriter}.
	 * @param writer Writes JSON.
	 * @throws ArgumentNullException Thrown if writer is null.
	 */
	public GraphJsonWriter(Writer writer)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(writer, "writer");
		this.writer = writer;
	}

	@Override
	public void write(Graph graph) throws ArgumentNullException, GraphPathSegmentInvalidException {
		Map<String, Boolean> configuration = new HashMap<>();
		configuration.put(JsonGenerator.PRETTY_PRINTING, true);
		final var jsonProvider = JsonProvider.provider();
		final var jsonWriterFactory = jsonProvider.createWriterFactory(configuration);
		final var jsonWriter = jsonWriterFactory.createWriter(this.writer);
		final var refactoringObjectBuilder = Json.createObjectBuilder();
		refactoringObjectBuilder.add("refactoringName", graph.getRefactoringName());
		
		final var nodes =
				graph
					.getNodes()
					.stream()
					.sorted((n1, n2) -> n1.getClass().getName().compareTo(n2.getClass().getName()))
					.collect(Collectors.toList());
		if (nodes.size() > 0) {
			final var nodesArrayBuilder = Json.createArrayBuilder();
			for (final var node : nodes) {
				final var nodeObject = buildNodeJsonObject(nodes, node);
				nodesArrayBuilder.add(nodeObject);
			}
			refactoringObjectBuilder.add("nodes", nodesArrayBuilder.build());
		}
		
		jsonWriter.writeObject(refactoringObjectBuilder.build());
	}
	
	private static JsonObject buildNodeJsonObject(
			List<GraphNode> nodes,
			GraphNode node) {
		final var objectBuilder = Json.createObjectBuilder();
		
		final var type = node.getClass().getName();
		objectBuilder.add("type", type);
		
		final var edges =
				node
					.getEdges()
					.stream()
					.sorted((e1, e2) -> e1.getClass().getName().compareTo(e2.getClass().getName()))
					.collect(Collectors.toList());
		if (edges.size() > 0) {
			final var edgesArrayBuilder = Json.createArrayBuilder();
			for (final var edge : edges) {
				final var edgeObjectBuilder = Json.createObjectBuilder();
				edgeObjectBuilder.add("type", edge.getClass().getName());
				
				final var destinationNodeIndex = nodes.indexOf(edge.getDestinationNode());
				edgeObjectBuilder.add("to", destinationNodeIndex);
				edgesArrayBuilder.add(edgeObjectBuilder.build());
			}
			
			objectBuilder.add("edges", edgesArrayBuilder.build());
		}
		
		return objectBuilder.build();
	}
}
