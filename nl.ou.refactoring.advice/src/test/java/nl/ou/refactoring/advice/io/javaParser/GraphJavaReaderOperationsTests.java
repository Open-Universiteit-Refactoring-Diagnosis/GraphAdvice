package nl.ou.refactoring.advice.io.javaParser;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartDirection;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartWriter;
import nl.ou.refactoring.advice.io.plantuml.classDiagrams.GraphPlantUmlClassDiagramWriter;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;

public final class GraphJavaReaderOperationsTests {
	private static Path OUTPUT_DIR;

	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output", "javaReader", "operations");
		Files.createDirectories(OUTPUT_DIR);
	}

	@DisplayName("Should read a combination of files regarding employees, focusing on operations and their callers")
	@Test
	public void readTest() throws IOException {
		final var refactoringName = "Employees test";
		final var mermaidFlowchartFilePath = OUTPUT_DIR.resolve(refactoringName + ".mermaid");
		final var plantUmlClassDiagramFilePath = OUTPUT_DIR.resolve(refactoringName + ".puml");

		final var graph = new Graph(refactoringName);
		final var resolutionProvider = new GraphJavaReaderResolutionProvider() {
			@Override
			public <T extends GraphNodeCode> Optional<T> resolveByFullyQualifiedName(
					Graph graph,
					String fullyQualifiedName,
					Class<T> resultType) {
				return Optional.empty();
			}
		};

		// Legacy Employee
		final var legacyEmployeeJavaCode = this.fromResource("javaParser/operations/employees/LegacyEmployee.java");
		final var legacyEmployeeJavaStringReader = new StringReader(legacyEmployeeJavaCode);
		final var legacyEmployeeFileNameFull = "$/javaParser/operations/employees/LegacyEmployee.java";
		final var legacyEmployeeFileName = "LegacyEmployee.java";
		final var legacyEmployeeJavaReader =
				new GraphJavaReader(
						graph,
						resolutionProvider,
						legacyEmployeeJavaStringReader,
						legacyEmployeeFileNameFull,
						legacyEmployeeFileName);
		legacyEmployeeJavaReader.read();

		// Employee
		final var employeeJavaCode = this.fromResource("javaParser/operations/employees/Employee.java");
		final var employeeJavaStringReader = new StringReader(employeeJavaCode);
		final var employeeFileNameFull = "$/javaParser/operations/employees/Employee.java";
		final var employeeFileName = "Employee.java";
		final var employeeJavaReader =
				new GraphJavaReader(
						graph,
						resolutionProvider,
						employeeJavaStringReader,
						employeeFileNameFull,
						employeeFileName);
		employeeJavaReader.read();

		// Company
		final var companyJavaCode = this.fromResource("javaParser/operations/employees/Company.java");
		final var companyJavaStringReader = new StringReader(companyJavaCode);
		final var companyFileNameFull = "$/javaParser/operations/employees/Company.java";
		final var companyFileName = "Company.java";
		final var companyJavaReader =
				new GraphJavaReader(
						graph,
						resolutionProvider,
						companyJavaStringReader,
						companyFileNameFull,
						companyFileName);
		companyJavaReader.read();

		try (final var mermaidFlowchartStringWriter = new StringWriter();
				final var mermaidFlowchartBufferedWriter =
						new BufferedWriter(new FileWriter(mermaidFlowchartFilePath.toFile()));) {
			new GraphMermaidFlowchartWriter(mermaidFlowchartStringWriter, GraphMermaidFlowchartDirection.LeftToRight)
					.write(graph);
			mermaidFlowchartBufferedWriter.write(mermaidFlowchartStringWriter.toString());
		} catch (IOException exception) {
			fail(String.format("Failed to write Mermaid Flowchart for graph '%s'", refactoringName));
		}

		// UML Class Diagram
		try (final var plantUmlClassDiagramStringWriter = new StringWriter();
				final var plantUmlClassDiagramBufferedWriter =
						new BufferedWriter(new FileWriter(plantUmlClassDiagramFilePath.toFile()));) {
			new GraphPlantUmlClassDiagramWriter(plantUmlClassDiagramStringWriter).write(graph);
			plantUmlClassDiagramBufferedWriter.write(plantUmlClassDiagramStringWriter.toString());
		} catch (IOException exception) {
			fail(String.format("Failed to write PlantUML Class Diagram for graph '%s'", refactoringName));
		}
	}

	private String fromResource(String path) throws IOException {
		try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {
			return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		}
	}
}
