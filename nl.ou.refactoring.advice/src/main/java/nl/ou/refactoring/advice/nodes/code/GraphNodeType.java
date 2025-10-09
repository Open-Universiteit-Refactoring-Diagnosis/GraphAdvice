package nl.ou.refactoring.advice.nodes.code;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a data type in a Refactoring Advice Graph.
 */
public final class GraphNodeType extends GraphNodeCode {
	private final String typeName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeType}.
	 * @param graph The graph that contains the node.
	 * @param typeName The name of the data type of code affected by a refactoring.
	 * @throws ArgumentNullException Thrown if graph or typeName is null.
	 * @throws ArgumentEmptyException Thrown if typeName is empty or contains only white spaces.
	 */
	public GraphNodeType(Graph graph, String typeName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(typeName, "typeName");
		this.typeName = typeName;
	}
	
	/**
	 * Gets the name of the data type of code affected by a refactoring.
	 * @return The name of the data type of code affected by a refactoring.
	 */
	public String getTypeName() {
		return this.typeName;
	}

	@Override
	public String getLabel() {
		return "Type";
	}

	@Override
	public String getCaption() {
		return this.typeName;
	}
}
