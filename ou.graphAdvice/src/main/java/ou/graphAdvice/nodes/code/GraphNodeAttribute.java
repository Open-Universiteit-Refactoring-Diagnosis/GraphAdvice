package ou.graphAdvice.nodes.code;

import ou.graphAdvice.Graph;
import ou.graphAdvice.contracts.ArgumentEmptyException;
import ou.graphAdvice.contracts.ArgumentGuard;
import ou.graphAdvice.contracts.ArgumentNullException;

/**
 * Represents a node in a Refactoring Advice Graph that describes an Attribute of a Class that is affected by a refactoring.
 */
public final class GraphNodeAttribute extends GraphNodeCode {
	private final String attributeName;
	
	/**
	 * Initialises a new instance of {@link GraphNodeAttribute}.
	 * @param graph The graph that contains the node.
	 * @param attributeName The name of the affected Attribute.
	 * @throws ArgumentNullException Thrown if graph or attributeName is null.
	 * @throws ArgumentEmptyException Thrown if attributeName is empty or contains only white spaces.
	 */
	public GraphNodeAttribute(Graph graph, String attributeName)
			throws ArgumentNullException, ArgumentEmptyException {
		super(graph);
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(attributeName, "attributeName");
		this.attributeName = attributeName;
	}
	
	/**
	 * Gets the name of the Attribute affected by a refactoring.
	 * @return The name of the Attribute affected by a refactoring.
	 */
	public String getAttributeName() {
		return this.attributeName;
	}
}
