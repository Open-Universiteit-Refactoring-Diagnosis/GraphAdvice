package nl.ou.refactoring.advice.io;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphPathSegmentInvalidException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Writes Refactoring Advice Graphs to a destination.
 */
public interface GraphWriter {
	/**
	 * Writes a Refactoring Advice Graph to a destination.
	 * @param graph The Refactoring Advice Graph to write.
	 * @throws ArgumentNullException Thrown if graph or layoutSettings is null.
	 * @throws GraphPathSegmentInvalidException Thrown if the graph contains a path with an invalid segment.
	 */
	void write(Graph graph)
		throws ArgumentNullException, GraphPathSegmentInvalidException;
}
