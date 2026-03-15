package nl.ou.refactoring.advice.nlp;

import nl.ou.refactoring.advice.Graph;
import nl.ou.refactoring.advice.GraphValidationException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Provides Natural Language Processing results.
 */
public abstract class NLPProcessor {
	/**
	 * Builds the clear text of the Natural Language Processing result.
	 */
	protected final StringBuilder stringBuilder;
	
	/**
	 * Initialises a new instance of {@link NLPProcessor}.
	 */
	protected NLPProcessor() {
		this.stringBuilder = new StringBuilder();
	}
	
	/**
	 * Processes the Refactoring Advice Graph to produce an {@link NLPResult}.
	 * @param graph The Refactoring Advice Graph to process.
	 * @return The Natural Language Processing result.
	 * @throws ArgumentNullException Thrown if graph is null.
	 * @throws GraphValidationException Thrown if the graph is invalid.
	 * @throws NLPException Thrown if the Natural Language Processing result failed.
	 */
	public abstract NLPResult process(Graph graph)
		throws
			ArgumentNullException,
			GraphValidationException,
			NLPException;
}