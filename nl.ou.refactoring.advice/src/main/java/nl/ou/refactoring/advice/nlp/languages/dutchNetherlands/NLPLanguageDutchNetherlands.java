package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.HashMap;
import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * The Dutch language in the Netherlands (ISO designation "nl-NL") for Natural Language Processing.
 */
public final class NLPLanguageDutchNetherlands implements NLPLanguage {
	/**
	 * The singleton instance of {@link NLPLanguageDutchNetherlands}.
	 */
	public static final NLPLanguageDutchNetherlands INSTANCE = new NLPLanguageDutchNetherlands();

	private NLPLanguageDutchNetherlands() { }

	@Override
	public NLPResult visit(Sentence sentence) {
		return new NLPResult("", new HashMap<String, GraphNode>());
	}
}