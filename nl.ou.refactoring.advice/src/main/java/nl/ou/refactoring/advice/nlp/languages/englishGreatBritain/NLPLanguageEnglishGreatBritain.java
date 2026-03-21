package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * The English language in Great Britain (ISO designation "en-GB") for Natural Language Processing.
 */
public final class NLPLanguageEnglishGreatBritain implements NLPLanguage {
	/**
	 * The singleton instance of {@link NLPLanguageEnglishGreatBritain}.
	 */
	public static final NLPLanguageEnglishGreatBritain INSTANCE = new NLPLanguageEnglishGreatBritain();

	private NLPLanguageEnglishGreatBritain() { }

	@Override
	public Function<Long, Optional<GrammaticalGender>> getNounGenderSuppliers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public NLPResult visit(Sentence sentence) {
		return new NLPResult("", new HashMap<String, GraphNode>());
	}
}
