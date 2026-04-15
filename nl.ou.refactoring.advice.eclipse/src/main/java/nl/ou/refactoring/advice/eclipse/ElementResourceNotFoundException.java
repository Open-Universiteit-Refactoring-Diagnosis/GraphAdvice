package nl.ou.refactoring.advice.eclipse;

import org.eclipse.jdt.core.IJavaElement;

/**
 * An exception that is thrown if a project element's resource could not be determined.
 */
public final class ElementResourceNotFoundException extends RuntimeException {
	/**
	 * Generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -4235218969823262688L;
	
	/**
	 * The element of which the resource could not be determined.
	 */
	private final IJavaElement element;

	/**
	 * Initialises a new instance of {@link ElementResourceNotFoundException}.
	 * @param element The element of which the resource could not be determined.
	 */
	public ElementResourceNotFoundException(IJavaElement element) {
		this.element = element;
	}
	
	/**
	 * Gets the element of which the resource could not be determined.
	 * @return The element of which the resource could not be determined.
	 */
	public IJavaElement getElement() {
		return this.element;
	}
}