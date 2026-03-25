package nl.ou.refactoring.advice.nlp.languages;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Noun;

/**
 * A language in Natural Language Processing.
 */
public interface NLPLanguage {
	/**
	 * Gets a supplier for the grammatical gender of the specified noun.
	 * @return {@link Supplier} The supplier for the grammatical gender of the specified noun, if found wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	Supplier<GrammaticalGender> getGenderSupplier(Noun noun);
	
	/**
	 * Visits a {@link Sentence} to provide a Natural Language Processing result.
	 * @param phrase The sentence to visit.
	 * @return The {@link NLPResult} that contains the result of Natural Language Processing.
	 */
	NLPResult visit(Sentence sentence);
}