package nl.ou.refactoring.advice.io.plantuml;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
public class GraphPlantUmlSvgGeneratorTests {
	@DisplayName("Should generate SVG from a PlantUML specification")
	@Test
	public void generateTest() throws ArgumentNullException, ArgumentEmptyException, IOException {
		// Arrange
		final var inputStream = GraphPlantUmlSvgGeneratorTests.class.getResourceAsStream("/plantuml/MoveField.puml");
		final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		final var scanner = new Scanner(bufferedReader).useDelimiter("\\A");
		final var puml = scanner.next();
		scanner.close();		
		// Act
		final var result = GraphPlantUmlSvgGenerator.generate(puml);
		// Assert
		assertNotNull(result);
	}
}