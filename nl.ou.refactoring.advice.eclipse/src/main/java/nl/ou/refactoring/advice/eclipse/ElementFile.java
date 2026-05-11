package nl.ou.refactoring.advice.eclipse;

import java.io.File;

import org.eclipse.jdt.core.IJavaElement;

/**
 * A file that belongs to a Java element.
 */
public final class ElementFile extends File {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -1077847106091926785L;
	
	/**
	 * The Java element.
	 */
	private IJavaElement element;
	
	/**
	 * Initialises a new instance of {@link ElementFile}.
	 * @param element The Java element to which the file belongs.
	 */
	public ElementFile(IJavaElement element) {
		super(extractPath(element));
		this.element = element;
	}
	
	/**
	 * Gets the Java element to which the file belongs.
	 * @return The Java element to which the file belongs.
	 */
	public IJavaElement getElement() {
		return this.element;
	}
	
	private static String extractPath(IJavaElement element)
			throws
				ElementResourceNotFoundException,
				ElementResourceLocationNotFoundException {
		final var resource = element.getResource();
		if (resource == null) {
			throw new ElementResourceNotFoundException(element);
		}
		
		final var resourceLocation = resource.getLocation();
		if (resourceLocation == null) {
			throw new ElementResourceLocationNotFoundException(element);
		}
		
		return resourceLocation.toOSString();
	}
}