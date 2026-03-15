package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;
import nl.ou.refactoring.advice.nlp.grammar.determiners.Determiner;

/**
 * Represents a Noun Phrase in Natural Language.
 */
public final class NounPhrase extends Phrase {
	private final Noun noun;
	private Optional<Determiner> determiner;
	
	/**
	 * Initialises a new instance of {@link NounPhrase}.
	 * @param noun The head noun of the noun phrase.
	 * @param determiner An optional determiner.
	 * @throws ArgumentNullException
	 */
	public NounPhrase(Noun noun, Determiner determiner)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(noun, "noun");
		this.noun = noun;
		this.determiner = Optional.ofNullable(determiner);
	}
	
	/**
	 * Initialises a new instance of {@link NounPhrase}.
	 * @param noun The head noun of the noun phrase.
	 */
	public NounPhrase(Noun noun)
			throws ArgumentNullException {
		this(noun, null);
	}
	
	/**
	 * Gets the head noun of the noun phrase.
	 * @return The head noun of the noun phrase.
	 */
	public Noun getNoun() {
		return this.noun;
	}
	
	/**
	 * Gets an optional determiner of the noun phrase.
	 * @return The optional determiner of the noun phrase.
	 */
	public Optional<Determiner> getDeterminer() {
		return this.determiner;
	}
}