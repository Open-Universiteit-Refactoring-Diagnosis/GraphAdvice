package nl.ou.refactoring.advice.io;

import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClass;
import nl.ou.refactoring.advice.nodes.code.classes.GraphNodeClassMember;
import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if a {@link GraphWriter} implementation needs a {@link GraphNodeClass} that is associated with a {@link GraphNodeClassMember},
 * but there is no edge from {@link GraphNodeClassMember} to a {@link GraphNodeClass}.
 */
public final class GraphWriterMemberOwningClassMissingException extends GraphWriterException {
	/**
	 * A generated serial version identifier of the exception.
	 */
	private static final long serialVersionUID = -2058850437359250405L;
	
	/**
	 * The {@link GraphNodeClassMember} that does not have an edge to a {@link GraphNodeClass}.
	 */
	private final GraphNodeClassMember memberNode;

	/**
	 * Initialises a new instance of {@link GraphWriterMemberOwningClassMissingException}.
	 * @param memberNode The {@link GraphNodeClassMember} that does not have an edge to a {@link GraphNodeClass}.
	 */
	public GraphWriterMemberOwningClassMissingException(GraphNodeClassMember memberNode) {
		this.memberNode = memberNode;
	}
	
	/**
	 * Gets the {@link GraphNodeClassMember} that does not have an edge to a {@link GraphNodeClass}.
	 * @return The {@link GraphNodeClassMember} that does not have an edge to a {@link GraphNodeClass}.
	 */
	public GraphNodeClassMember getMemberNode() {
		return this.memberNode;
	}
	
	@Override
	public String getLocalizedMessage() {
		final var messageTemplate =
			ResourceProvider
				.ExceptionMessages
				.getMessageTemplate(GraphWriterMemberOwningClassMissingException.class);
		return String.format(messageTemplate, this.memberNode.getId());
	}
}