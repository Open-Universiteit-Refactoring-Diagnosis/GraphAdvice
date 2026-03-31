package nl.ou.refactoring.advice.io.html;

import java.net.URI;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Settings for a {@link GraphHtmlWriter}.
 */
public class GraphHtmlWriterSettings {
	private final URI resourceUrlPrefix;
	
	/**
	 * Initialises a new instance of {@link GraphHtmlWriterSettings}.
	 * @param resourceUrlPrefix The prefix for the resource Uniform Resource Locator (URL) that leads to syntax elements that are referred to in the generated HTML.
	 * @throws ArgumentNullException Thrown if resourceUrlPrefix is null.
	 */
	public GraphHtmlWriterSettings(URI resourceUrlPrefix)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(resourceUrlPrefix, "resourceUrlPrefix");
		this.resourceUrlPrefix = resourceUrlPrefix;
	}

	/**
	 * Gets the prefix for the resource Uniform Resource Locator (URL) that leads to syntax elements that are referred to in the generated HTML.
	 * @return The prefix for the resource Uniform Resource Locator (URL) that leads to syntax elements that are referred to in the generated HTML.
	 */
	public final URI getResourceUrlPrefix() {
		return this.resourceUrlPrefix;
	}
}