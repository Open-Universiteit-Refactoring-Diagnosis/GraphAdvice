package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.adjectives.Adjective;
import nl.ou.refactoring.advice.nlp.grammar.determiners.Determiner;

/**
 * Represents a Noun Phrase in Natural Language.
 */
public final class NounPhrase extends Phrase {
	private final Noun noun;
	private Optional<Determiner> determiner;
	private List<Adjective> adjectives;
	private NounDeclensionKey declension = NounDeclensionKey.DEFAULT;
	
	/**
	 * Initialises a new instance of {@link NounPhrase}.
	 * @param noun The head noun of the noun phrase.
	 */
	public NounPhrase(Noun noun)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(noun, "noun");
		this.noun = noun;
		this.determiner = Optional.empty();
		this.adjectives = new ArrayList<Adjective>();
	}
	
	/**
	 * Gets the adjectives in the Noun Phrase.
	 * @return An unmodifiable list of {@link Adjective}.
	 */
	public List<Adjective> getAdjectives() {
		return Collections.unmodifiableList(this.adjectives);
	}
	
	/**
	 * Gets an optional determiner of the noun phrase.
	 * @return The optional determiner of the noun phrase.
	 */
	public Optional<Determiner> getDeterminer() {
		return this.determiner;
	}
	
	/**
	 * Gets the declension of the {@link NounPhrase}.
	 * @return The {@link NounDeclensionKey} that specifies the declension of the {@link NounPhrase}.
	 */
	public NounDeclensionKey getDeclension() {
		return this.declension;
	}
	
	/**
	 * Sets the declension of the {@link NounPhrase}.
	 * @param declension {@link NounDeclensionKey} Specifies the declension of the {@link NounPhrase}.
	 * @throws ArgumentNullException Thrown if declension is null.
	 */
	public void setDeclension(NounDeclensionKey declension)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(declension, "key");
		this.declension = declension;
	}
	
	/**
	 * Gets the head noun of the noun phrase.
	 * @return The head noun of the noun phrase.
	 */
	public Noun getNoun() {
		return this.noun;
	}
}