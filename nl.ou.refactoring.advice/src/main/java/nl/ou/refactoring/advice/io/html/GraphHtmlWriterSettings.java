package nl.ou.refactoring.advice.io.html;

import java.net.URI;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

public class GraphHtmlWriterSettings {
	private final URI resourceUrlPrefix;
	
	public GraphHtmlWriterSettings(URI resourceUrlPrefix)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(resourceUrlPrefix, "resourceUrlPrefix");
		this.resourceUrlPrefix = resourceUrlPrefix;
	}

	public final URI getResourceUrlPrefix() {
		return this.resourceUrlPrefix;
	}
}
