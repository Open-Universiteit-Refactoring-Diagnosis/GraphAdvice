package nl.ou.refactoring.advice.nlp.grammar.verbs;

import nl.ou.refactoring.advice.nlp.grammar.NLPGrammarException;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;

/**
 * An exception that is thrown if a Noun Phrase is set in a Verb Phrase when the
 * main Verb is intransitive.
 */
public final class VerbIntransitiveCannotHaveNounPhraseException extends NLPGrammarException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = -4833247757519430509L;

	/**
	 * The Verb that is the subject of the exception.
	 */
	private final Verb verb;

	/**
	 * The Noun Phrase that is being added to a Verb Phrase with the specified Verb.
	 */
	private final NounPhrase nounPhrase;

	/**
	 * Initialises a new instance of
	 * {@link VerbIntransitiveCannotHaveNounPhraseException}.
	 * 
	 * @param verb       The Verb that is the subject of the exception.
	 * @param nounPhrase The Noun Phrase that is being added to a Verb Phrase with
	 *                   the specified Verb.
	 */
	public VerbIntransitiveCannotHaveNounPhraseException(Verb verb, NounPhrase nounPhrase) {
		this.verb = verb;
		this.nounPhrase = nounPhrase;
	}

	/**
	 * Gets the Verb that is the subject of the exception.
	 * 
	 * @return The Verb that is the subject of the exception.
	 */
	public Verb getVerb() {
		return this.verb;
	}

	/**
	 * Gets the Noun Phrase that is being added to a Verb Phrase with the specified
	 * Verb.
	 * 
	 * @return The Noun Phrase that is being added to a Verb Phrase with the
	 *         specified Verb.
	 */
	public NounPhrase getNounPhrase() {
		return this.nounPhrase;
	}
}
