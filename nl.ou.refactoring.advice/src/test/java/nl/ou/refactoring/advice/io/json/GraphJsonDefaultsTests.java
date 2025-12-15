package nl.ou.refactoring.advice.io.json;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphReaderException;
import nl.ou.refactoring.advice.io.images.GraphPainter;
import nl.ou.refactoring.advice.io.layouts.forceDirected.GraphLayoutForceDirectedSettings;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartDirection;
import nl.ou.refactoring.advice.io.mermaid.flowcharts.GraphMermaidFlowchartWriter;

public final class GraphJsonDefaultsTests {
	private static Path OUTPUT_DIR;
	
	@BeforeAll
	static void setUp() throws IOException {
		OUTPUT_DIR = Paths.get("target", "test-output");
		Files.createDirectories(OUTPUT_DIR);
	}
	
	@Test
	@DisplayName("Should load Rename Field refactoring")
	public void renameFieldTest()
			throws
				GraphReaderException,
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				IOException {
		final var renameFieldGraph = GraphJsonDefaults.renameField();
		assertNotNull(renameFieldGraph);
		
		// Image
		final var graphPainter = new GraphPainter(1024, 768);
		final var layoutSettings = new GraphLayoutForceDirectedSettings();
		layoutSettings.setSpringLength(200.0);
		layoutSettings.setRepulsionConstant(2000.0);
		final var renameFieldImage =
				(RenderedImage)graphPainter.createImage(renameFieldGraph, layoutSettings);
		final var renameFieldImageFilePath =
				OUTPUT_DIR.resolve("RenameField.png");
		ImageIO.write(renameFieldImage, "png", renameFieldImageFilePath.toFile());
		
		// Mermaid
		final var mermaidFlowchartStringWriter = new StringWriter();
		final var mermaidFlowchartWriter =
				new GraphMermaidFlowchartWriter(
						mermaidFlowchartStringWriter,
						GraphMermaidFlowchartDirection.LeftToRight);
		mermaidFlowchartWriter.write(renameFieldGraph);
		final var renameFieldFlowchartFilePath =
				OUTPUT_DIR.resolve("RenameField_Flowchart.md");
		final var mermaidFlowchartBufferedWriter =
				new BufferedWriter(new FileWriter(renameFieldFlowchartFilePath.toFile()));
		mermaidFlowchartBufferedWriter.write(mermaidFlowchartStringWriter.toString());
		mermaidFlowchartBufferedWriter.close();
	}
	
	@Test
	@DisplayName("Should load Rename Method refactoring")
	public void renameMethodTest()
			throws
				GraphReaderException,
				ArgumentNullException,
				GraphPathSegmentInvalidException,
				IOException {
		final var renameMethodGraph = GraphJsonDefaults.renameMethod();
		assertNotNull(renameMethodGraph);
		
		// Image
		final var graphPainter = new GraphPainter(1024, 768);
		final var layoutSettings = new GraphLayoutForceDirectedSettings();
		layoutSettings.setSpringLength(200.0);
		layoutSettings.setRepulsionConstant(2000.0);
		final var renameMethodImage =
				(RenderedImage)graphPainter.createImage(renameMethodGraph, layoutSettings);
		final var renameMethodImageFilePath =
				OUTPUT_DIR.resolve("RenameMethod.png");
		ImageIO.write(renameMethodImage, "png", renameMethodImageFilePath.toFile());
		
		// Mermaid
		final var mermaidFlowchartStringWriter = new StringWriter();
		final var mermaidFlowchartWriter =
				new GraphMermaidFlowchartWriter(
						mermaidFlowchartStringWriter,
						GraphMermaidFlowchartDirection.LeftToRight);
		mermaidFlowchartWriter.write(renameMethodGraph);
		final var renameMethodFlowchartFilePath =
				OUTPUT_DIR.resolve("RenameMethod_Flowchart.md");
		final var mermaidFlowchartBufferedWriter =
				new BufferedWriter(new FileWriter(renameMethodFlowchartFilePath.toFile()));
		mermaidFlowchartBufferedWriter.write(mermaidFlowchartStringWriter.toString());
		mermaidFlowchartBufferedWriter.close();
	}
}
