package nl.ou.refactoring.advice.nodes.code.tokens;

import java.util.regex.Pattern;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.contracts.ArgumentPatternException;
import nl.ou.refactoring.advice.nodes.GraphNodeBase;

/**
 * Represents an identifier in code syntax.
 */
public final class Identifier
		extends GraphNodeBase
		implements ExpressionName
{
	private final static Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_$][a-zA-Z\\\\d_$]*");
	
	/**
	 * The identifier value.
	 */
	private final String identifier;

	/**
	 * Initialises a new instance of {@link Identifier}.
	 * @param identifier The identifier value.
	 * @throws ArgumentNullException Thrown if graph is null.
	 * @throws ArgumentPatternException Thrown if identifier does not contain a valid value.
	 */
	public Identifier(Graph graph, String identifier)
			throws ArgumentNullException, ArgumentPatternException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requirePattern(identifier, IDENTIFIER_PATTERN, "identifier");
		super(graph);
		this.identifier = identifier;
	}

	@Override
	public GraphNodeBase clone(Graph graph) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(graph, "graph");
		return new Identifier(graph, this.identifier);
	}
	
	@Override
	public String getLabel() {
		return "Identifier";
	}
	
	/**
	 * Gets the identifier value.
	 * @return The identifier value.
	 */
	public String getIdentifier() {
		return this.identifier;
	}
}