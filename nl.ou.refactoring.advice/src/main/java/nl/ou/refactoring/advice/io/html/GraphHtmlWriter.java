package nl.ou.refactoring.advice.io.html;

import org.w3c.dom.html.HTMLElement;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriter;

/**
 * Writes refactoring advice texts to HTML from Refactoring Advice Graphs.
 */
public abstract class GraphHtmlWriter implements GraphWriter {
	protected final HTMLElement hostElement;

	/**
	 * Initialises a new instance of {@link GraphHtmlWriter}.
	 * @param hostElement The host HTML element that will contain the advice.
	 * @throws ArgumentNullException
	 */
	public GraphHtmlWriter(HTMLElement hostElement)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(hostElement, "hostElement");
		this.hostElement = hostElement;
	}
	
	/**
	 * Gets the host HTML element that will contain the advice.
	 * @return The host HTML element that will contain the advice.
	 */
	public HTMLElement getHostElement() {
		return this.hostElement;
	}
}