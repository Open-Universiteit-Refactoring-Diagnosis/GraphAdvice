package nl.ou.refactoring.advice.io.images;

import java.awt.image.BufferedImage;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.io.GraphWriter;
import nl.ou.refactoring.advice.io.layouts.GraphLayoutSettings;

/**
 * Writes Refactoring Advice Graphs to images.
 */
public final class GraphImageWriter implements GraphWriter {
	private final BufferedImage image;
	
	/**
	 * Initialises a new instance of {@link GraphImageWriter}.
	 * @param image The image surface to write on.
	 */
	public GraphImageWriter(BufferedImage image) {
		ArgumentGuard.requireNotNull(image, "image");
		this.image = image;
	}

	@Override
	public void Write(Graph graph, GraphLayoutSettings layoutSettings)
			throws ArgumentNullException, GraphPathSegmentInvalidException {
		ArgumentGuard.requireNotNull(graph, "graph");
		ArgumentGuard.requireNotNull(layoutSettings, "layoutSettings");
		final var width = this.image.getWidth();
		final var height = this.image.getHeight();
		final var painter = new GraphPainter(width, height);
		final var graphics = this.image.createGraphics();
		painter.draw(graph, graphics, layoutSettings);
	}
}
