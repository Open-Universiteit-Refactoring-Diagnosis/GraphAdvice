package nl.ou.refactoring.advice.nlp.grammar.adjectives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;

/**
 * Represents an Adjective Phrase in Natural Language.
 */
public final class AdjectivePhrase extends Phrase {
	/**
	 * A list of {@link Adjective} in the Adjective Phrase.
	 */
	private final List<Adjective> adjectives;

	/**
	 * Initialises a new instance of an {@link AdjectivePhrase}.
	 */
	public AdjectivePhrase() {
		this.adjectives = new ArrayList<>();
	}

	/**
	 * Gets the adjectives in the adjective phrase.
	 * 
	 * @return An unmodifiable list of {@link Adjective}.
	 */
	public List<Adjective> getAdjectives() {
		return Collections.unmodifiableList(this.adjectives);
	}

	/**
	 * Adds an adjective to the Adjective Phrase.
	 * 
	 * @param adjective The adjective to add.
	 * @return True if the adjective has been added, otherwise false.
	 * @throws ArgumentNullException Thrown if adjective is null.
	 */
	public boolean addAdjective(Adjective adjective) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(adjective, "adjective");
		return this.adjectives.add(adjective);
	}

	/**
	 * Removes an adjective from the Adjective Phrase.
	 * 
	 * @param adjective The adjective to remove.
	 * @return True if the adjective has been removed, otherwise false.
	 * @throws ArgumentNullException Thrown if adjective is null.
	 */
	public boolean removeAdjective(Adjective adjective) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(adjective, "adjective");
		return this.adjectives.remove(adjective);
	}

	/**
	 * Sets the adjectives in the adjective phrase.
	 * 
	 * @param adjectives The adjectives in the adjective phrase.
	 * @return True if the adjectives have been set, otherwise false.
	 * @throws ArgumentNullException Thrown if adjectives is null.
	 */
	public boolean setAdjectives(List<Adjective> adjectives) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(adjectives, "adjectives");
		this.adjectives.clear();
		return this.adjectives.addAll(adjectives);
	}
}