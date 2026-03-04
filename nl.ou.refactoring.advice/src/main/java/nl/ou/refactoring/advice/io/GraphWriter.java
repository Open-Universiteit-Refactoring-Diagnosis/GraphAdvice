package nl.ou.refactoring.advice.io;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Writes Refactoring Advice Graphs to a destination.
 */
public interface GraphWriter {
	/**
	 * Writes a Refactoring Advice Graph to a destination.
	 * @param graph The Refactoring Advice Graph to write.
	 * @throws ArgumentNullException Thrown if graph or layoutSettings is null.
	 * @throws GraphValidationException Thrown if the graph is invalid.
	 * @throws GraphWriterException Thrown if the writer fails.
	 */
	void write(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			GraphWriterException;
}
