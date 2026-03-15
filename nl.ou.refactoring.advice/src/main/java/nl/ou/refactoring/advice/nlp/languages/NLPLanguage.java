package nl.ou.refactoring.advice.nlp.languages;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;

/**
 * A language in Natural Language Processing.
 */
public interface NLPLanguage {
	/**
	 * Visits a {@link Sentence} to provide a Natural Language Processing result.
	 * @param phrase The sentence to visit.
	 * @return The {@link NLPResult} that contains the result of Natural Language Processing.
	 */
	NLPResult visit(Sentence sentence);
}