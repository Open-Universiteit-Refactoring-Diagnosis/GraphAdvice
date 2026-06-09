package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.List;
import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.adjectives.Adjective;
import nl.ou.refactoring.advice.nlp.grammar.adjectives.AdjectivePhrase;
import nl.ou.refactoring.advice.nlp.grammar.determiners.Determiner;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;

/**
 * Represents a Noun Phrase in Natural Language.
 */
public final class NounPhrase extends Phrase {
	private final Noun noun;
	private Optional<Determiner> determiner;
	private Optional<AdjectivePhrase> adjectivePhrase;
	private Optional<PrepositionalPhrase> prepositionalPhrase;
	private NounDeclensionKey declension = NounDeclensionKey.DEFAULT;

	/**
	 * Initialises a new instance of {@link NounPhrase}.
	 * 
	 * @param noun The head noun of the noun phrase.
	 */
	public NounPhrase(Noun noun) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(noun, "noun");
		this.noun = noun;
		this.determiner = Optional.empty();
		this.adjectivePhrase = Optional.empty();
		this.prepositionalPhrase = Optional.empty();
	}

	/**
	 * Gets the adjectives in the Noun Phrase.
	 * 
	 * @return An unmodifiable list of {@link Adjective}.
	 */
	public List<Adjective> getAdjectives() {
		return this.adjectivePhrase.map(AdjectivePhrase::getAdjectives).orElse(List.of());
	}

	/**
	 * Gets the {@link AdjectivePhrase} of the {@link NounPhrase}.
	 * 
	 * @return The {@link AdjectivePhrase} of the {@link NounPhrase}, wrapped in
	 *         {@link Optional} if present, otherwise an empty {@link Optional}.
	 */
	public Optional<AdjectivePhrase> getAdjectivePhrase() {
		return this.adjectivePhrase;
	}

	/**
	 * Gets an optional determiner of the noun phrase.
	 * 
	 * @return The optional determiner of the noun phrase.
	 */
	public Optional<Determiner> getDeterminer() {
		return this.determiner;
	}

	/**
	 * Gets the declension of the {@link NounPhrase}.
	 * 
	 * @return The {@link NounDeclensionKey} that specifies the declension of the
	 *         {@link NounPhrase}.
	 */
	public NounDeclensionKey getDeclension() {
		return this.declension;
	}

	/**
	 * Gets the head noun of the noun phrase.
	 * 
	 * @return The head noun of the noun phrase.
	 */
	public Noun getNoun() {
		return this.noun;
	}

	/**
	 * Gets the {@link PrepositionalPhrase} of the {@link NounPhrase}, if present
	 * wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * 
	 * @return An {@link Optional} that wraps the {@link PrepositionalPhrase} or
	 *         empty if not present.
	 */
	public Optional<PrepositionalPhrase> getPrepositionalPhrase() {
		return this.prepositionalPhrase;
	}

	/**
	 * Sets the {@link AdjectivePhrase} of the {@link NounPhrase}.
	 * 
	 * @param adjectivePhrase The {@link AdjectivePhrase} to set in the
	 *                        {@link NounPhrase}. If null, the backing field is set
	 *                        to an empty {@link Optional}.
	 */
	public void setAdjectivePhrase(AdjectivePhrase adjectivePhrase) {
		this.adjectivePhrase = Optional.ofNullable(adjectivePhrase);
	}

	/**
	 * Sets the declension of the {@link NounPhrase}.
	 * 
	 * @param declension {@link NounDeclensionKey} Specifies the declension of the
	 *                   {@link NounPhrase}.
	 * @throws ArgumentNullException Thrown if declension is null.
	 */
	public void setDeclension(NounDeclensionKey declension) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(declension, "key");
		this.declension = declension;
	}

	/**
	 * Sets the {@link PrepositionalPhrase} of the {@link NounPhrase}.
	 * 
	 * @param prepositionalPhrase The {@link PrepositionalPhrase} to set in the
	 *                            {@link NounPhrase}. If null, the backing field is
	 *                            set to an empty {@link Optional}.
	 */
	public void setPrepositionalPhrase(PrepositionalPhrase prepositionalPhrase) {
		this.prepositionalPhrase = Optional.ofNullable(prepositionalPhrase);
	}
}