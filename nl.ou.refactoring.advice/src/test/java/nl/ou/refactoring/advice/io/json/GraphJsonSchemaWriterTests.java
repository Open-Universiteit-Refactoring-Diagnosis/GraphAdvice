package nl.ou.refactoring.advice.io.json;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public final class GraphJsonSchemaWriterTests {
	private static Path OUTPUT_DIR;

	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output");
		Files.createDirectories(OUTPUT_DIR);
	}
	
	@Test
	@DisplayName("Should write JSON Schema for the representation of Refactoring Advice Graphs")
	public void writeTest() throws IOException {
		final var outputJsonPath = OUTPUT_DIR.resolve("json");
		if (!Files.exists(outputJsonPath)) {
			Files.createDirectory(OUTPUT_DIR.resolve("json"));
		}
		final var jsonSchemaFilePath = OUTPUT_DIR.resolve(Path.of("json", "RefactoringAdviceGraph.schema.json"));
		try (
			final var jsonSchemaBufferedWriter =
				new BufferedWriter(new FileWriter(jsonSchemaFilePath.toFile()));
		) {
			new GraphJsonSchemaWriter(jsonSchemaBufferedWriter).write();
			jsonSchemaBufferedWriter.flush();
		}
	}
}
