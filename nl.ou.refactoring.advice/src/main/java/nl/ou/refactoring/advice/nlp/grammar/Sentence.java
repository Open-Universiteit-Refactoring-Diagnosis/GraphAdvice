package nl.ou.refactoring.advice.nlp.grammar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Sentence in Natural Language.
 */
public final class Sentence implements SyntaxElement {
	private final Set<Phrase> phrases;
	
	/**
	 * Initialises a new instance of {@link Sentence}.
	 */
	public Sentence() {
		this.phrases = new HashSet<Phrase>();
	}
	
	/**
	 * Adds a new {@link Phrase} to the {@link Sentence}.
	 * @param phrase The {@link Phrase} to add to the {@link Sentence}.
	 */
	public void addPhrase(Phrase phrase) {
		this.phrases.add(phrase);
	}
	
	/**
	 * Gets an unmodifiable set of {@link Phrase}s in the {@link Sentence}.
	 * @return An unmodifiable set of {@link Phrase}s in the {@link Sentence}.
	 */
	public Set<Phrase> getPhrases() {
		return Collections.unmodifiableSet(this.phrases);
	}
}