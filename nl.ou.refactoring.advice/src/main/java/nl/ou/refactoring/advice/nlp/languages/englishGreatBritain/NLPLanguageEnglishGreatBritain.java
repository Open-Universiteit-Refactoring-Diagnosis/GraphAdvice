package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.HashMap;

import nl.ou.refactoring.advice.nlp.NLPResult;
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
	public NLPResult visit(Sentence sentence) {
		return new NLPResult("", new HashMap<String, GraphNode>());
	}

}
