package nl.ou.refactoring.advice;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import nl.ou.refactoring.advice.edges.GraphEdge;

/**
 * An exception that is thrown if multiple Edges of the same type are being connected to the same source Node and the same destination Node.
 */
public final class MultipleEdgesOfSameTypeException extends Exception {
	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = -34632506627229242L;
	
	/**
	 * The edge that is of the same type on the source node and destination node.
	 */
	private final GraphEdge edge;
	
	/**
	 * Initialises a new instance of {@link MultipleEdgesOfSameTypeException}.
	 * @param edge {@link GraphEdge} The Edge of the same type.
	 */
	public MultipleEdgesOfSameTypeException(GraphEdge edge) {
		super();
		this.edge = edge;
	}
	
	/**
	 * Gets a localised exception message that contains relevant details.
	 * @return A localised exception message that contains relevant details.
	 */
	@Override
	public String getLocalizedMessage() {
		final var sourceNode = this.edge.getSourceNode();
		final var destinationNode = this.edge.getDestinationNode();
		final var messageFormat =
				ResourceBundle
					.getBundle("ExceptionMessages")
					.getString("multipleEdgesOfSameType");
		return
				MessageFormat.format(
						messageFormat,
						this.edge.getClass().getName(),
						sourceNode.getId().toString(),
						destinationNode.getId().toString());
	}

}
