package nl.ou.refactoring.advice.io.json;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

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
	private static final String INDENT = "\t";
	private final PrintWriter printWriter;
	private int indentIndex = 0;
	
	/**
	 * Initialises a new instance of {@link GraphJsonWriter}.
	 * @param writer Writes JSON.
	 * @throws ArgumentNullException Thrown if writer is null.
	 */
	public GraphJsonWriter(Writer writer)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(writer, "writer");
		this.printWriter = new PrintWriter(writer);
	}

	@Override
	public void write(Graph graph) throws ArgumentNullException, GraphPathSegmentInvalidException {
		this.printLine("{");
		this.indentIndex++;
		this.printProperty("refactoringName", graph.getRefactoringName());
		final var nodes =
				graph
					.getNodes()
					.stream()
					.sorted((n1, n2) -> n1.getClass().getName().compareTo(n2.getClass().getName()))
					.collect(Collectors.toList());
		if (nodes.size() > 0) {
			this.printLine("\"nodes\": [");
			this.indentIndex++;
			var nodeCounter = 0;
			for (final var node : graph.getNodes()) {
				this.printNode(nodes, node, nodeCounter);
				nodeCounter++;
			}
			this.indentIndex--;
			this.printLine("],");
		}
		this.indentIndex--;
		this.printLine("}");
	}
	
	private void printLine(String text) {
		this.printWriter.println(INDENT.repeat(indentIndex) + text);
	}
	
	private void printProperty(String propertyName, String propertyValue) {
		this.printLine(String.format("\"%s\": \"%s\",", propertyName, propertyValue));
	}
	
	private void printProperty(String propertyName, int propertyValue) {
		this.printLine(String.format("\"%s\": %d,", propertyName, propertyValue));
	}
	
	private void printNode(List<GraphNode> nodes, GraphNode node, int nodeCounter) {
		this.printLine("{");
		this.indentIndex++;
		
		this.printProperty("index", nodeCounter);
		
		final var type = node.getClass().getName();
		this.printProperty("type", type);
		
		final var edges =
				node
					.getEdges()
					.stream()
					.sorted((e1, e2) -> e1.getClass().getName().compareTo(e2.getClass().getName()))
					.collect(Collectors.toList());
		if (edges.size() > 0) {
			this.printLine("\"edges\": [");
			this.indentIndex++;
			for (final var edge : edges) {
				this.printLine("{");
				this.indentIndex++;
				this.printProperty("type", edge.getClass().getName());
				final var destinationNodeIndex = nodes.indexOf(edge.getDestinationNode());
				this.printProperty("to", destinationNodeIndex);
				this.indentIndex--;
				this.printLine("},");
			}
			this.indentIndex--;
			this.printLine("],");
		}
		
		this.indentIndex--;
		this.printLine("},");
	}
}
