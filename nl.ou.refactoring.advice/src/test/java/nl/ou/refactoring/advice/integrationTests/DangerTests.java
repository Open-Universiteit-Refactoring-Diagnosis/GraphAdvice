package nl.ou.refactoring.advice.integrationTests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartDirection;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartWriter;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;
import nl.ou.refactoring.advice.io.text.GraphTextWriter;
import nl.ou.refactoring.advice.nlp.processors.NLPConcatenationProcessor;

public final class DangerTests {
	private static Path OUTPUT_DIR;

	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output", "dangers");
		Files.createDirectories(OUTPUT_DIR);
	}

	@DisplayName("Should properly generate a flowchart, class diagram and advice text for dangers")
	@ParameterizedTest
	@ArgumentsSource(DangerTestsArgumentsProvider.class)
	public void writeFlowchartClassDiagramAndTextTest(Graph graph)
			throws IOException, ArgumentNullException, GraphPathSegmentInvalidException {
	    final var refactoringName = graph.getRefactoringName();
	    final var mermaidFlowchartFilePath = OUTPUT_DIR.resolve(refactoringName + ".mermaid");
	    final var plantUmlClassDiagramFilePath = OUTPUT_DIR.resolve(refactoringName + ".puml");
	    final var concatenatedAdviceFilePath = OUTPUT_DIR.resolve(refactoringName + ".txt");

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
	    
	    // Concatenated text
	    try (
	    		final var concatenatingStringWriter =
	    			new StringWriter();
	    		final var concatenatingBufferedWriter =
	    			new BufferedWriter(new FileWriter(concatenatedAdviceFilePath.toFile()))
	    ) {
	    	new GraphTextWriter(concatenatingStringWriter, new NLPConcatenationProcessor()).write(graph);
	    	concatenatingBufferedWriter.write(concatenatingStringWriter.toString());
	    } catch (IOException exception) {
	    	fail(String.format("Failed to write concatenated text advice for graph '%s'", refactoringName));
	    }
	}
}