package nl.ou.refactoring.advice.io.html.text;

import java.util.TreeMap;
import org.w3c.dom.Element;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriterException;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriter;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriterSettings;
import nl.ou.refactoring.advice.nlp.NLPProcessor;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;

/**
 * Writes refactoring advice texts in HTML from a Refactoring Advice Graph.
 */
public final class GraphHtmlTextWriter extends GraphHtmlWriter {
	private final NLPProcessor nlpProvider;
	
	/**
	 * Initialises a new instance of {@link GraphHtmlTextWriter}.
	 * @param settings The settings of the {@link GraphHtmlTextWriter}.
	 * @param element The host HTML element that will contain the refactoring advice text.
	 * @param nlpProvider Provides Natural Language Processing features.
	 * @throws ArgumentNullException Thrown if settings, hostElement or nlpProvider is null.
	 */
	public GraphHtmlTextWriter(GraphHtmlWriterSettings settings, Element hostElement, NLPProcessor nlpProvider)
			throws ArgumentNullException {
		super(settings, hostElement);
		ArgumentGuard.requireNotNull(nlpProvider, "nlpProvider");
		this.nlpProvider = nlpProvider;
	}
	
	/**
	 * Gets the Natural Language Processing provider that provides natural language processing features.
	 * @return The Natural Language Processing provider that provides natural language processing features.
	 */
	public NLPProcessor getNLPProvider() {
		return this.nlpProvider;
	}

	@Override
	public void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException {
		ArgumentGuard.requireNotNull(graph, "graph");
		
		// Natural Language Processing
		final var nlpResult = this.nlpProvider.process(graph);
		final var text = nlpResult.getText();
		final var references = nlpResult.getReferences();
		
		// <article> element
		final var document = this.hostElement.getOwnerDocument();
		final var articleElement = document.createElement("article");
		this.hostElement.appendChild(articleElement);
		
		// Divide text into parts along the references.
		final var indexWithReferenceMap = new TreeMap<Integer, String>();
		final var referenceEntries = references.entrySet();
		for (final var referenceEntry : referenceEntries) {
			final var referenceString = referenceEntry.getKey();
			var indexPrevious = 0;
			while (indexPrevious >= 0 && indexPrevious < text.length()) {
				var indexNext = text.indexOf(referenceString, indexPrevious);
				if (indexNext < 0) {
					break;
				}
				indexWithReferenceMap.put(indexNext, referenceString);
				indexPrevious = indexNext + referenceString.length();
			}
		}
		
		var indexPrevious = 0;
		final var indexWithReferenceMapEntries = indexWithReferenceMap.entrySet();
		for (final var indexWithReferenceEntry : indexWithReferenceMapEntries) {
			final var indexReference = indexWithReferenceEntry.getKey();
			final var textBefore = text.substring(indexPrevious, indexReference);
			if (textBefore.length() > 0) {
				articleElement.appendChild(document.createTextNode(textBefore));
			}
			final var reference = indexWithReferenceEntry.getValue();
			final var referenceNode = references.get(reference);
			if (referenceNode instanceof GraphNodeCode) {
				final var referenceNodeCode = (GraphNodeCode)referenceNode;
				final var programLocationNodeOptional = referenceNodeCode.getProgramLocationNode();
				if (programLocationNodeOptional.isPresent()) {
					final var programLocationNode = programLocationNodeOptional.get();
					final var referenceFileNameFull = programLocationNode.getFileNameFull();
					final var anchorElement = document.createElement("a");
					final var link = this.settings.getResourceUrlPrefix().toString() + referenceFileNameFull;
					anchorElement.setAttribute("href", link);
					anchorElement.setTextContent(referenceNode.getCaption());
					articleElement.appendChild(anchorElement);
				} else {
					articleElement.appendChild(document.createTextNode(referenceNode.getCaption()));
				}
			} else {
				articleElement.appendChild(document.createTextNode(referenceNode.getCaption()));
			}
			indexPrevious = indexPrevious + textBefore.length() + reference.length();
		}
		
		// Append last bit of plain text if the last reference occurrence wasn't at the end of the text.
		if (indexWithReferenceMapEntries.size() > 0 && indexPrevious < text.length()) {
			articleElement.appendChild(document.createTextNode(text.substring(indexPrevious, text.length())));
		}
	}
}