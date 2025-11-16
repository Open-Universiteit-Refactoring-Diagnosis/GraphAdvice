package nl.ou.refactoring.advice.io.json;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;

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
		final var image = (RenderedImage)graphPainter.createImage(renameFieldGraph, layoutSettings);
		final var imageFile = new File("C:\\Test\\RenameField.png");
		imageFile.delete();
		ImageIO.write(image, "png", imageFile);
		
		// Mermaid
		final var mermaidFlowchartStringWriter = new StringWriter();
		final var mermaidFlowchartWriter =
				new GraphMermaidFlowchartWriter(
						mermaidFlowchartStringWriter,
						GraphMermaidFlowchartDirection.LeftToRight);
		mermaidFlowchartWriter.write(renameFieldGraph);
		final var mermaidFlowchartFile = new File("C:\\Test\\RenameField_Flowchart.md");
		mermaidFlowchartFile.delete();
		mermaidFlowchartFile.createNewFile();
		final var mermaidFlowchartBufferedWriter = new BufferedWriter(new FileWriter(mermaidFlowchartFile));
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
		final var image = (RenderedImage)graphPainter.createImage(renameMethodGraph, layoutSettings);
		final var imageFile = new File("C:\\Test\\RenameMethod.png");
		imageFile.delete();
		ImageIO.write(image, "png", imageFile);
		
		// Mermaid
		final var mermaidFlowchartStringWriter = new StringWriter();
		final var mermaidFlowchartWriter =
				new GraphMermaidFlowchartWriter(
						mermaidFlowchartStringWriter,
						GraphMermaidFlowchartDirection.LeftToRight);
		mermaidFlowchartWriter.write(renameMethodGraph);
		final var mermaidFlowchartFile = new File("C:\\Test\\RenameMethod_Flowchart.md");
		mermaidFlowchartFile.delete();
		mermaidFlowchartFile.createNewFile();
		final var mermaidFlowchartBufferedWriter = new BufferedWriter(new FileWriter(mermaidFlowchartFile));
		mermaidFlowchartBufferedWriter.write(mermaidFlowchartStringWriter.toString());
		mermaidFlowchartBufferedWriter.close();
	}
}
