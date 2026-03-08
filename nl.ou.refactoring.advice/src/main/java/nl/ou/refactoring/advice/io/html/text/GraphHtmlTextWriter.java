package nl.ou.refactoring.advice.io.html.text;

import org.w3c.dom.html.HTMLElement;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriter;

/**
 * Writes refactoring advice texts in HTML from a Refactoring Advice Graph.
 */
public final class GraphHtmlTextWriter extends GraphHtmlWriter {
	/**
	 * Initialises a new instance of {@link GraphHtmlTextWriter}.
	 * @param hostElement The host HTML element that will contain the refactoring advice text.
	 * @throws ArgumentNullException Thrown if hostElement is null.
	 */
	public GraphHtmlTextWriter(HTMLElement hostElement)
			throws ArgumentNullException {
		super(hostElement);
	}

	@Override
	public void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException {
		// TODO Auto-generated method stub
		
	}
}