package nl.ou.refactoring.advice.nlp;

import java.util.HashMap;
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
	
	public NLPResult merge(NLPResult otherResult, String separator) {
		ArgumentGuard.requireNotNull(otherResult, "otherResult");
		ArgumentGuard.requireNotNull(separator, "separator");
		return merge(this, otherResult, separator);
	}
	
	/**
	 * Merges this {@link NLPResult} with the specified other {@link NLPResult}.
	 * @param otherResult The other {@link NLPResult}.
	 * @return The merged {@link NLPResult}.
	 * @throws ArgumentNullException Thrown if otherResult is null.
	 */
	public NLPResult merge(NLPResult otherResult)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(otherResult, "otherResult");
		return merge(this, otherResult, "");
	}
	
	/**
	 * Merges two {@link NLPResult} into one {@link NLPResult}.
	 * @param firstResult The first {@link NLPResult}.
	 * @param secondResult The second {@link NLPResult}.
	 * @return The merged {@link NLPResult}.
	 * @throws ArgumentNullException Thrown if firstResult or secondResult is null.
	 */
	public static NLPResult merge(NLPResult firstResult, NLPResult secondResult, String separator)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(firstResult, "firstResult");
		ArgumentGuard.requireNotNull(secondResult, "secondResult");
		ArgumentGuard.requireNotNull(separator, "separator");
		final var text = firstResult.getText() + separator + secondResult.getText();
		final var references = new HashMap<String, GraphNode>();
		final var referencesEntriesFirst = firstResult.getReferences().entrySet();
		for (final var entry : referencesEntriesFirst) {
			references.put(entry.getKey(), entry.getValue());
		}
		final var referencesEntriesSecond = secondResult.getReferences().entrySet();
		for (final var entry : referencesEntriesSecond) {
			references.putIfAbsent(entry.getKey(), entry.getValue());
		}
		return new NLPResult(text, references);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !NLPResult.class.isInstance(obj)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final var other = (NLPResult)obj;
		return this.getText().equals(other.getText()) && this.getReferences().equals(other.getReferences());
	};
}