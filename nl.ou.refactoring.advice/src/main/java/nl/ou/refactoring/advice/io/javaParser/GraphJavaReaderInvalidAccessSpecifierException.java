package nl.ou.refactoring.advice.io.javaParser;

import java.text.MessageFormat;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;

import nl.ou.refactoring.advice.resources.ResourceProvider;

/**
 * An exception that is thrown if an invalid {@link AccessSpecifier} was read
 * for a {@link TypeDeclaration}.
 */
public class GraphJavaReaderInvalidAccessSpecifierException extends GraphJavaReaderException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 7480391672348952417L;
	private final AccessSpecifier accessSpecifier;
	private final NodeWithAccessModifiers<?> nodeWithAccessModifiers;

	/**
	 * Initialises a new instance of
	 * {@link GraphJavaReaderInvalidAccessSpecifierException}.
	 * 
	 * @param accessSpecifier The invalid access specifier.
	 * @param typeDeclaration The type declaration with which the access specifier
	 *                        is associated.
	 */
	public GraphJavaReaderInvalidAccessSpecifierException(
			AccessSpecifier accessSpecifier,
			NodeWithAccessModifiers<?> typeDeclaration) {
		this.accessSpecifier = accessSpecifier;
		this.nodeWithAccessModifiers = typeDeclaration;
	}

	/**
	 * Gets the invalid access specifier.
	 * 
	 * @return The invalid access specifier.
	 */
	public AccessSpecifier getAccessSpecifier() {
		return this.accessSpecifier;
	}

	/**
	 * Gets the node with which the access specifier is associated.
	 * 
	 * @return The node with which the access specifier is associated.
	 */
	public NodeWithAccessModifiers<?> getNodeWithAccessModifiers() {
		return this.nodeWithAccessModifiers;
	}

	@Override
	public String getLocalizedMessage() {
		final var messageTemplate = ResourceProvider.ExceptionMessages.getMessageTemplate(this.getClass());
		return MessageFormat.format(messageTemplate, this.accessSpecifier, this.nodeWithAccessModifiers);
	}
}