package nl.ou.refactoring.advice.io;

import nl.ou.refactoring.advice.Graph;

/**
 * Reads Refactoring Advice Graphs.
 */
public interface GraphReader {
	/**
	 * Reads a Refactoring Advice Graph from a source.
	 * @return The Refactoring Advice Graph that was read from the source.
	 * @throws GraphReaderException Thrown if a fault is encountered during reading.
	 */
	Graph read() throws GraphReaderException;
}