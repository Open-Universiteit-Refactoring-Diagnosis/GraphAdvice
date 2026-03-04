package nl.ou.refactoring.advice.io.svg.classDiagrams;

import java.io.StringWriter;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.svg.GraphSvgSettings;
import nl.ou.refactoring.advice.io.svg.GraphSvgWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodePackage;

/**
 * Writes Class Diagrams in SVG from a Refactoring Advice Graph.
 */
public final class GraphSvgClassDiagramWriter extends GraphSvgWriter {
	/**
	 * The settings for the SVG writer and content.
	 */
	private final GraphSvgSettings settings;
	
	/**
	 * Initialises a new instance of {@link GraphSvgClassDiagramWriter}.
	 * @param stringWriter Writes the SVG content.
	 * @param settings The settings for the SVG writer and content.
	 * @throws ArgumentNullException Thrown if stringWriter is null.
	 */
	public GraphSvgClassDiagramWriter(StringWriter stringWriter, GraphSvgSettings settings)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(stringWriter, "stringWriter");
		ArgumentGuard.requireNotNull(settings, "settings");
		super(stringWriter);
		this.settings = settings;
	}

	@Override
	public void write(Graph graph)
			throws
				ArgumentNullException,
				GraphValidationException,
				GraphWriterException {
		final var svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		final var svg = SVGDOMImplementation.getDOMImplementation();
		final var svgDocument = svg.createDocument(svgNS, "svg", null);
		final var svgRoot = svgDocument.getDocumentElement();
		svgRoot.setAttributeNS(null, "width", String.valueOf(this.settings.getWidth()));
		svgRoot.setAttributeNS(null, "height", String.valueOf(this.settings.getHeight()));
		
		final var packages =
			graph
				.getNodes(GraphNodePackage.class);
		// TODO rest
	}
	
	/**
	 * Gets the settings for the SVG writer and content.
	 * @return The settings for the SVG writer and content.
	 */
	public GraphSvgSettings getSettings() {
		return this.settings;
	}
}