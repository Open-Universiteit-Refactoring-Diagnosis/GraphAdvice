package nl.ou.refactoring.advice.nodes.code;

import java.text.MessageFormat;

import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a {@link GraphNodeClass} has multiple class nodes as generalisations.
 */
public final class GraphNodeClassHasMultipleGeneralisationsException
		extends GraphValidationException {
	private final GraphNodeClass classNode;

	/**
	 * A generated serial version UID for serialisation purposes.
	 */
	private static final long serialVersionUID = 8000131016528446763L;

	/**
	 * Initialises a new instance of {@link GraphNodeClassHasMultipleGeneralisationsException}.
	 * @param classNode The class node that has multiple generalisations associated with it.
	 * @throws ArgumentNullException Thrown if classNode is null.
	 */
	public GraphNodeClassHasMultipleGeneralisationsException(GraphNodeClass classNode)
			throws ArgumentNullException {
		super();
		ArgumentGuard.requireNotNull(classNode, "classNode");
		this.classNode = classNode;
	}
	
	/**
	 * Gets the class node that has multiple generalisations associated with it.
	 * @return The class node that has multiple generalisations associated with it.
	 */
	public GraphNodeClass getClassNode() {
		return this.classNode;
	}

	/**
	 * Gets a localised exception message that contains relevant details.
	 * @return A localised exception message that contains relevant details.
	 */
	@Override
	public String getLocalizedMessage() {
		final var messageFormat =
				ResourceProvider
					.ExceptionMessages
					.getMessageTemplate(this.getClass());
		return
				MessageFormat.format(
						messageFormat,
						this.classNode.getClassName()
				);
	}

}
