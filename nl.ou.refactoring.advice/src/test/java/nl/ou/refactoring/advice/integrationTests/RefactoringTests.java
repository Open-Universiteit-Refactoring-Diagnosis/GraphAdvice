package nl.ou.refactoring.advice.integrationTests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.html.GraphHtmlWriterSettings;
import nl.ou.refactoring.advice.io.html.text.GraphHtmlTextWriter;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartDirection;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartWriter;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;
import nl.ou.refactoring.advice.nlp.NLPProcessor;

public final class RefactoringTests {
	private static Path OUTPUT_DIR;

	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output", "refactorings");
		Files.createDirectories(OUTPUT_DIR);
	}
	
	@DisplayName("Should properly generate a flowchart, class diagram and advice text for refactorings")
	@ParameterizedTest
	@ArgumentsSource(RefactoringTestsArgumentsProvider.class)
	public void writeRefactoringOutputsTest(Graph graph, List<NLPProcessor> nlpProcessors)
			throws
				IOException,
				ArgumentNullException,
				GraphValidationException,
				URISyntaxException,
				ParserConfigurationException,
				TransformerException {
	    final var refactoringName = graph.getRefactoringName();
	    final var mermaidFlowchartFilePath = OUTPUT_DIR.resolve(refactoringName + ".mermaid");
	    final var plantUmlClassDiagramFilePath = OUTPUT_DIR.resolve(refactoringName + ".puml");
	    
	    // Flowchart (graph)
	    try (
	    		final var mermaidFlowchartStringWriter =
	    			new StringWriter();
	    		final var mermaidFlowchartBufferedWriter =
	    			new BufferedWriter(new FileWriter(mermaidFlowchartFilePath.toFile()));
	    ) {
	    	new GraphMermaidFlowchartWriter(
	    			mermaidFlowchartStringWriter,
	    			GraphMermaidFlowchartDirection.LeftToRight
	    	).write(graph);
	    	mermaidFlowchartBufferedWriter.write(mermaidFlowchartStringWriter.toString());
	    } catch (IOException exception) {
	    	fail(String.format("Failed to write Mermaid Flowchart for graph '%s'", refactoringName));
	    }
	    
	    // UML Class Diagram
	    try (
	    		final var plantUmlClassDiagramStringWriter =
	    			new StringWriter();
	    		final var plantUmlClassDiagramBufferedWriter =
	    			new BufferedWriter(new FileWriter(plantUmlClassDiagramFilePath.toFile()));
	    ) {
	        new GraphPlantUmlClassDiagramWriter(plantUmlClassDiagramStringWriter).write(graph);
	        plantUmlClassDiagramBufferedWriter.write(plantUmlClassDiagramStringWriter.toString());
	    } catch (IOException exception) {
	        fail(String.format("Failed to write PlantUML Class Diagram for graph '%s'", refactoringName));
	    }
	    
	    // HTML text
	    for (final var nlpProcessor : nlpProcessors) {
	    	final var htmlFilePath = OUTPUT_DIR.resolve(refactoringName + "_" + nlpProcessor.toString() + ".html");
		    try (
			    final var concatenatingBufferedWriter =
			    	new BufferedWriter(new FileWriter(htmlFilePath.toFile()))
			) {
				final var htmlWriterSettings = new GraphHtmlWriterSettings(new URI("eclipse-resource://nl.ou.refactoring.advice.eclipse/"));
					
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
				
				new GraphHtmlTextWriter(htmlWriterSettings, htmlBodyElement, nlpProcessor).write(graph);
					
				final var transformerFactory = TransformerFactory.newInstance();
				final var transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					
				transformer.transform(new DOMSource(htmlDocument), new StreamResult(concatenatingBufferedWriter));
				concatenatingBufferedWriter.flush();
		    } catch (IOException exception) {
			    fail(String.format("Failed to write concatenated text advice for graph '%s'", refactoringName));
		    }
	    }
	}
}