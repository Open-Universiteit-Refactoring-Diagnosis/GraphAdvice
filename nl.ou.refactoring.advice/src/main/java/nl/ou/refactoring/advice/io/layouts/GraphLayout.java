package nl.ou.refactoring.advice.io.layouts;

import java.awt.geom.Rectangle2D;
import java.util.Set;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A layout of a Refactoring Advice Graph.
 */
public abstract class GraphLayout {
	private final GraphLayoutSettings settings;
	
	/**
	 * Initialises a new instance of {@link GraphLayout}.
	 * @param settings The layout settings.
	 * @throws ArgumentNullException Thrown if settings is null.
	 */
	public GraphLayout(GraphLayoutSettings settings)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(settings, "settings");
		this.settings = settings;
	}

	/**
	 * Gets the layout settings.
	 * @return The layout settings.
	 */
	public GraphLayoutSettings getSettings() {
		return this.settings;
	}
	
	/**
	 * Applies the layout on the specified Refactoring Advice Graph in parameter graph.
	 * @param graph The Refactoring Advice Graph to apply the layout to.
	 * @param area The area to map the layout to.
	 * @return A set of layout nodes.
	 * @throws ArgumentNullException Thrown if graph or area is null.
	 * @throws GraphPathSegmentInvalidException Thrown if a graph path has an invalid segment.
	 */
	public abstract Set<GraphLayoutNode> apply(Graph graph, Rectangle2D area)
			throws ArgumentNullException, GraphPathSegmentInvalidException;
}
