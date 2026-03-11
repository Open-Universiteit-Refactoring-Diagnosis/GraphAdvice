package nl.ou.refactoring.advice.io.html.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriterSettings;
import nl.ou.refactoring.advice.nlp.NLPProvider;
import nl.ou.refactoring.advice.nlp.providers.NLPConcatenationProvider;

public final class GraphHtmlTextWriterTests {
	@DisplayName("Should write HTML for the Refactoring Advice text")
	@ParameterizedTest
	@ArgumentsSource(GraphHtmlTextWriterTestsWriteArgumentsProvider.class)
	public void writeTests(Graph graph, String htmlStringExpected)
		throws
			ArgumentNullException,
			ParserConfigurationException,
			TransformerException,
			URISyntaxException {
		// Arrange
		final var settings = new GraphHtmlWriterSettings(new URI("eclipse-resource://nl.ou.refactoring.advice.eclipse/"));
		
		final var htmlDocument =
			DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.newDocument();
		final var htmlElement = htmlDocument.createElement("html");
		htmlDocument.appendChild(htmlElement);
		final var htmlHeadElement = htmlDocument.createElement("head");
		htmlElement.appendChild(htmlHeadElement);
		final var htmlBodyElement = htmlDocument.createElement("body");
		htmlElement.appendChild(htmlBodyElement);
		
		final var nlpProvider = new NLPConcatenationProvider();
		final var htmlTextWriter = new GraphHtmlTextWriter(settings, htmlBodyElement, (NLPProvider)nlpProvider);
		
		// Act
		htmlTextWriter.write(graph);
		
		// Assert
		final var transformerFactory = TransformerFactory.newInstance();
		final var transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		final var resultWriter = new StringWriter();
		transformer.transform(new DOMSource(htmlDocument), new StreamResult(resultWriter));
		final var htmlString = resultWriter.toString();
		assertNotNull(htmlString);
		assertEquals(htmlStringExpected, htmlString);
	}
}