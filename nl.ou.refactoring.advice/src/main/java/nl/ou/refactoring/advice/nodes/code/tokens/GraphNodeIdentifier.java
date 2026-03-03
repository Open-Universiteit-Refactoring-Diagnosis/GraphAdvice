package nl.ou.refactoring.advice.nodes.code.tokens;

import java.util.regex.Pattern;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;
import nl.ou.refactoring.advice.edges.code.operations.expressions.GraphEdgeReferences;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;
import nl.ou.refactoring.advice.nodes.code.GraphNodeAttribute;

/**
 * Represents an identifier in code syntax.
 */
public final class GraphNodeIdentifier
		extends GraphNodeBase
		implements GraphNodeExpressionName
{
	/**
	 * The pattern for a valid Java identifier.
	 */
	private final static Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_$][a-zA-Z\\d_$]*$");
	
	/**
	 * The identifier value.
	 */
	private final String identifier;

	/**
	 * Initialises a new instance of {@link GraphNodeIdentifier}.
	 * @param identifier The identifier value.
	 * @throws ArgumentNullException Thrown if graph is null.
	 * @throws ArgumentPatternException Thrown if identifier does not contain a valid value.
	 */
	public GraphNodeIdentifier(Graph graph, String identifier)
			throws ArgumentNullException, ArgumentPatternException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requirePattern(identifier, IDENTIFIER_PATTERN, "identifier");
		super(graph);
		this.identifier = identifier;
	}
	
	
	/**
	 * Gets the identifier value.
	 * @return The identifier value.
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new GraphNodeIdentifier(graph, this.identifier);
	}
	
	@Override
	public boolean equals(GraphNode other) {
		if (!(other instanceof GraphNodeIdentifier)) {
			return false;
		}
		return this.getIdentifier().equals(((GraphNodeIdentifier)other).getIdentifier());
	}
	
	@Override
	public String getLabel() {
		return "Identifier";
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
		return
			this.graph.getOrAddEdge(
				this,
				attributeNode,
				(source, destination) -> new GraphEdgeReferences(source, destination),
				GraphEdgeReferences.class
			);
	}
}