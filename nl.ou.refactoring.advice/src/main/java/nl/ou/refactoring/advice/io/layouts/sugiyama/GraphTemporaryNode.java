package nl.ou.refactoring.advice.io.layouts.sugiyama;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * Represents a temporary node of a graph in a Sugiyama (Layered) Layout.
 */
public class GraphTemporaryNode extends GraphNode {
	/**
	 * Initialises a new instance of {@link GraphTemporaryNode}.
	 * @param graph The graph that contains the temporary node.
	 * @throws ArgumentNullException Thrown if graph is null.
	 */
	protected GraphTemporaryNode(Graph graph)
			throws ArgumentNullException {
		super(graph);
	}

	@Override
	public String getLabel() {
		return "Sugiyama";
	}

	@Override
	public String getCaption() {
		return "Temporary";
	}
}
