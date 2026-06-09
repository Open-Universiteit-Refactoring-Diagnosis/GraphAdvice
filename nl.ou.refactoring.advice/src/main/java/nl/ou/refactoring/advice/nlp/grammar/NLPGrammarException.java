package nl.ou.refactoring.advice.nlp.grammar;

import nl.ou.refactoring.advice.nlp.NLPException;

/**
 * An exception that is thrown when a grammatical inconsistency is detected.
 */
public abstract class NLPGrammarException extends NLPException {
	/**
	 * A generated serial version unique identifier.
	 */
	private static final long serialVersionUID = 6154198646073569369L;
	
	/**
	 * Initialises a new instance of {@link NLPGrammarException}.
	 */
	protected NLPGrammarException() {
	}
}