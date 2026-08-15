package nl.ou.refactoring.advice.api;

import java.net.URI;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.provider.Arguments;
import org.w3c.dom.Element;

import nl.ou.refactoring.advice.Graph;

public final class GenerateHtmlTestArguments implements Arguments {
	private final Graph graph;
	private final Element containerElement;
	private final URI resourceUriPrefix;

	GenerateHtmlTestArguments(Graph graph, Element containerElement, URI resourceUriPrefix) {
		this.graph = graph;
		this.containerElement = containerElement;
		this.resourceUriPrefix = resourceUriPrefix;
	}

	@Override
	public @Nullable Object[] get() {
		return new Object[] { this.graph, this.containerElement, this.resourceUriPrefix };
	}
}