package nl.ou.refactoring.advice.nlp;

import java.util.Map;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * A result of a Natural Language Processing request.
 */
public final class NLPResult {
	private final String text;
	private final Map<String, GraphNode> references;

	/**
	 * Initialises a new instance of {@link NLPResult}.
	 * @param text The clear text of the Natural Language Processing result.
	 * @param references The references in the text of the Natural Language Processing result.
	 * @throws ArgumentNullException Thrown if text or references is null.
	 */
	public NLPResult(String text, Map<String, GraphNode> references)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNull(text, "text");
		ArgumentGuard.requireNotNull(references, "references");
		this.text = text;
		this.references = references;
	}
	
	/**
	 * Gets the clear text of the Natural Language Processing result.
	 * @return The clear text of the Natural Language Processing result.
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Gets the references in the text of the Natural Language Processing result.
	 * @return The references in the text of the Natural Language Processing result.
	 */
	public Map<String, GraphNode> getReferences() {
		return this.references;
	}
}