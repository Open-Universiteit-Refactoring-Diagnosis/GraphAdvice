package nl.ou.refactoring.advice.nlp.languages;

import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;

/**
 * A language in Natural Language Processing.
 */
public interface NLPLanguage {
	/**
	 * Gets the producer for Grammatical Genders of Nouns based on their token.
	 * @return The producer for Grammatical Genders of Nouns based on their token.
	 */
	Function<Long, Optional<GrammaticalGender>> getNounGenderSuppliers();
	
	/**
	 * Visits a {@link Sentence} to provide a Natural Language Processing result.
	 * @param phrase The sentence to visit.
	 * @return The {@link NLPResult} that contains the result of Natural Language Processing.
	 */
	NLPResult visit(Sentence sentence);
}