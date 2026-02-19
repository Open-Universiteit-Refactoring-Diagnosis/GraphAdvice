package nl.ou.refactoring.advice.io.json;

import nl.ou.refactoring.advice.edges.GraphEdge;
import nl.ou.refactoring.advice.nodes.GraphNode;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if an edge was specified in JSON but no matching constructor was found for that edge.
 */
public final class GraphJsonReaderEdgeConstructorNoMatchException extends GraphJsonReaderException {

	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 3816614330150419327L;
	
	private final Class<? extends GraphEdge> edgeClassType;
	private final GraphNode sourceNode;
	private final GraphNode destinationNode;

	/**
	 * Initialises a new instance of {@link GraphJsonReaderEdgeConstructorNoMatchException}.
	 * @param edgeClassType The Class type of the edge that should be constructed.
	 * @param sourceNode The source node of the edge.
	 * @param destinationNode The destination node of the edge.
	 */
	public GraphJsonReaderEdgeConstructorNoMatchException
	(
		Class<? extends GraphEdge> edgeClassType,
		GraphNode sourceNode,
		GraphNode destinationNode
	)
	{
		this.edgeClassType = edgeClassType;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
	}
	
	@Override
	public String getLocalizedMessage() {
		return
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(GraphJsonReaderEdgeConstructorNoMatchException.class);
	}
	
	/**
	 * Gets the Class type of the edge that should be constructed.
	 * @return The Class type of the edge that should be constructed.
	 */
	public Class<? extends GraphEdge> getEdgeClassType() {
		return this.edgeClassType;
	}
	
	/**
	 * Gets the source node of the edge.
	 * @return The source node of the edge.
	 */
	public GraphNode getSourceNode() {
		return this.sourceNode;
	}
	
	/**
	 * Gets the destination node of the edge.
	 * @return The destination node of the edge.
	 */
	public GraphNode getDestinationNode() {
		return this.destinationNode;
	}
}