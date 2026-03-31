package nl.ou.refactoring.advice.io.html;

import org.w3c.dom.Element;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriter;

/**
 * Writes refactoring advice texts to HTML from Refactoring Advice Graphs.
 */
public abstract class GraphHtmlWriter implements GraphWriter {
	/**
	 * The settings for the {@link GraphHtmlWriter}.
	 */
	protected final GraphHtmlWriterSettings settings;
	
	/**
	 * The host HTML element that will contain the advice.
	 */
	protected final Element hostElement;

	/**
	 * Initialises a new instance of {@link GraphHtmlWriter}.
	 * @param settings The settings for the {@link GraphHtmlWriter}.
	 * @param hostElement The host HTML element that will contain the advice.
	 * @throws ArgumentNullException Thrown if settings or hostElement is null.
	 */
	protected GraphHtmlWriter(GraphHtmlWriterSettings settings, Element hostElement)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(settings, "settings");
		ArgumentGuard.requireNotNull(hostElement, "hostElement");
		this.settings = settings;
		this.hostElement = hostElement;
	}
	
	/**
	 * Gets the settings for the {@link GraphHtmlWriter}.
	 * @return The settings for the {@link GraphHtmlWriter}.
	 */
	public GraphHtmlWriterSettings getSettings() {
		return this.settings;
	}
	
	/**
	 * Gets the host HTML element that will contain the advice.
	 * @return The host HTML element that will contain the advice.
	 */
	public final Element getHostElement() {
		return this.hostElement;
	}
}