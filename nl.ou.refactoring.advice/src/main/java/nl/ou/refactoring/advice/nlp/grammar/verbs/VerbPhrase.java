package nl.ou.refactoring.advice.nlp.grammar.verbs;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.Phrase;

/**
 * Represents a Verb Phrase in Natural Language grammar.
 */
public final class VerbPhrase extends Phrase {
	private final Verb verb;
	
	/**
	 * Initialises a new instance of {@link VerbPhrase}.
	 * @param verb The verb of the verb phrase.
	 * @throws ArgumentNullException Thrown if verb is null.
	 */
	public VerbPhrase(Verb verb) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(verb, "verb");
		this.verb = verb;
	}
	
	/**
	 * Gets the verb of the verb phrase.
	 * @return The verb of the verb phrase.
	 */
	public Verb getVerb() {
		return this.verb;
	}
}