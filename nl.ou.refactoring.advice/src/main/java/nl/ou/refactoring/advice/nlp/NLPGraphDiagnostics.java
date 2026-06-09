package nl.ou.refactoring.advice.nlp;

import nl.ou.refactoring.advice.GraphPath;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostep;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepAdd;
import nl.ou.refactoring.advice.nodes.workflow.microsteps.GraphNodeMicrostepRemove;

/**
 * Diagnostics of a Refactoring Advice Graph (RAG) to determine what kind of
 * graph the Natural Language Processor is dealing with and to improve the
 * generated result.
 */
public final class NLPGraphDiagnostics {
	/**
	 * Initialises a new instance of {@link NLPGraphDiagnostics}. Scoped to
	 * "private" because the Class is intended to be static.
	 */
	private NLPGraphDiagnostics() {
	}

	/**
	 * Determines whether the Natural Language Processor is dealing with a Rename
	 * refactoring.
	 * 
	 * @param graphPath The path through the Refactoring Advice Graph (RAG) from the
	 *                  start node to a relevant result.
	 * @return A boolean that indicates whether the Refactoring Advice Graph (RAG)
	 *         concerns a rename.
	 */
	public static boolean isRenameRefactoring(GraphPath graphPath) {
		final var graphPathSegments = graphPath.getSegments();
		for (final var graphPathSegment : graphPathSegments) {
			final var nodeCurrent = graphPathSegment.getNode();
			if (nodeCurrent instanceof GraphNodeMicrostep) {
				final var microstep = (GraphNodeMicrostep) nodeCurrent;
				final var microstepsRelated = microstep.getRelated();

				if (microstep instanceof GraphNodeMicrostepAdd
						&& microstepsRelated.stream().anyMatch(GraphNodeMicrostepRemove.class::isInstance)) {
					// TODO this needs to be refined, because it could also be a move refactoring.
					return true;
				}
				if (microstep instanceof GraphNodeMicrostepRemove
						&& microstepsRelated.stream().anyMatch(GraphNodeMicrostepAdd.class::isInstance)) {
					// TODO this needs to be refined, because it could also be a move refactoring.
					return true;
				}
			}
		}
		return false;
	}
}
