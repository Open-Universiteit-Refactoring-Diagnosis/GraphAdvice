package nl.ou.refactoring.advice.eclipse;

import org.eclipse.jdt.core.IJavaElement;

/**
 * An exception that is thrown if a project element resource's location could not be determined.
 */
public final class ElementResourceLocationNotFoundException extends RuntimeException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 8648253660792184825L;
	
	/**
	 * The element of which the resource location could not be determined.
	 */
	private final IJavaElement element;

	/**
	 * Initialises a new instance of {@link ElementResourceLocationNotFoundException}.
	 * @param element The element of which the resource location could not be determined.
	 */
	public ElementResourceLocationNotFoundException(IJavaElement element) {
		this.element = element;
	}
	
	/**
	 * Gets the element of which the resource location could not be determined.
	 * @return The element of which the resource location could not be determined.
	 */
	public IJavaElement getElement() {
		return this.element;
	}
}
