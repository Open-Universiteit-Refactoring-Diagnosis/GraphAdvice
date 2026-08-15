package nl.ou.refactoring.advice.nodes.code.tokens;

import java.util.regex.Pattern;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeReferences;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * Represents an identifier in code syntax.
 */
public final class GraphNodeIdentifier extends GraphNodeBase implements GraphNodeExpressionName {
	/**
	 * The pattern for a valid Java identifier.
	 */
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_$][a-zA-Z\\d_$]*$");

	/**
	 * The identifier value.
	 */
	private final String identifier;

	/**
	 * Initialises a new instance of {@link GraphNodeIdentifier}.
	 * 
	 * @param graph      The Refactoring Advice Graph to which the identifier
	 *                   belongs.
	 * @param identifier The identifier value.
	 * @throws ArgumentNullException    Thrown if graph is null.
	 * @throws ArgumentPatternException Thrown if identifier does not contain a
	 *                                  valid value.
	 */
	private GraphNodeIdentifier(Graph graph, String identifier) throws ArgumentNullException, ArgumentPatternException {
		super(graph);
		ArgumentGuard.requirePattern(identifier, IDENTIFIER_PATTERN, "identifier");
		this.identifier = identifier;
	}

	/**
	 * Adds a new identifier node to the specified graph if it does not exist yet,
	 * otherwise it returns the existing identifier node.
	 * 
	 * @param graph      The Refactoring Advice Graph to which the identifier
	 *                   belongs.
	 * @param identifier The identifier value.
	 * @return A new identifier node if an equivalent node did not already exist,
	 *         otherwise the equivalent existing node.
	 * @throws ArgumentNullException    Thrown if graph or identifier is null.
	 * @throws ArgumentPatternException Thrown if the identifier value does not
	 *                                  conform to a Java identifier.
	 */
	public static GraphNodeIdentifier computeIdentifier(Graph graph, String identifier)
			throws ArgumentNullException, ArgumentEmptyException, ArgumentPatternException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(identifier, "identifier");
		return graph.getNodes(GraphNodeIdentifier.class)
				.stream()
				.filter(node -> node.getIdentifier().equals(identifier))
				.findAny()
				.orElseGet(() -> new GraphNodeIdentifier(graph, identifier));
	}

	/**
	 * Gets the identifier value.
	 * 
	 * @return The identifier value.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return graph.getNodes()
				.stream()
				.filter(GraphNodeIdentifier.class::isInstance)
				.map(GraphNodeIdentifier.class::cast)
				.filter((node) -> node.getIdentifier().equals(this.identifier))
				.findAny()
				.orElse(new GraphNodeIdentifier(graph, this.identifier));
	}

	@Override
	public boolean equals(GraphNode other) {
		if (!(other instanceof GraphNodeIdentifier)) {
			return false;
		}
		return this.getIdentifier().equals(((GraphNodeIdentifier) other).getIdentifier());
	}

	@Override
	public String getLabel() {
		return ResourceProvider.GraphNodeLabels.getLabel(this.getClass());
	}

	@Override
	public String getCaption() {
		return this.identifier;
	}

	@Override
	public String toString() {
		return this.identifier;
	}

	@Override
	public GraphEdgeReferences references(GraphNodeAttribute attributeNode) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(attributeNode, "attributeNode");
		return this.graph.computeEdge(
				this,
				attributeNode,
				(source, destination) -> new GraphEdgeReferences(source, destination),
				GraphEdgeReferences.class);
	}
}