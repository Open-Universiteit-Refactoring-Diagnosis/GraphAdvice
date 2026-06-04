package nl.ou.refactoring.advice.io.javaParser;

import java.util.Optional;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.nodes.code.GraphNodeCode;

/**
 * A supporting interface that resolves additional code symbols by request.
 */
public interface GraphJavaReaderResolutionProvider {
	/**
	 * Resolves a Graph node that may not have been loaded in the Refactoring Advice Graph (RAG) yet.
	 * @param <T> The type of Graph node expected in the resolution.
	 * @param graph The Refactoring Advice Graph (RAG) used for resolution.
	 * @param fullyQualifiedName The Fully Qualified Name of the requested code symbol.
	 * @param resultType The type of Graph node expected in the resolution.
	 * @return The Graph node, wrapped in an {@link Optional}, if resolved, otherwise an empty {@link Optional}.
	 */
	<T extends GraphNodeCode> Optional<T> resolveByFullyQualifiedName(
		Graph graph,
		String fullyQualifiedName,
		Class<T> resultType
	);
}