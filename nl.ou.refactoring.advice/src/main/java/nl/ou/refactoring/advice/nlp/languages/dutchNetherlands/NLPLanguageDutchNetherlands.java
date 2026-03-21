package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * The Dutch language in the Netherlands (ISO designation "nl-NL") for Natural Language Processing.
 */
public final class NLPLanguageDutchNetherlands implements NLPLanguage {
	/**
	 * The singleton instance of {@link NLPLanguageDutchNetherlands}.
	 */
	public static final NLPLanguageDutchNetherlands INSTANCE = new NLPLanguageDutchNetherlands();
	
	private final Map<Long, GrammaticalGender> nounGenders = new HashMap<Long, GrammaticalGender>();

	private NLPLanguageDutchNetherlands() {
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.REFACTORING, GrammaticalGender.MASCULINE);
	}
	
	@Override
	public Function<Long, Optional<GrammaticalGender>> getNounGenderSuppliers() {
		return (token) -> Optional.ofNullable(this.nounGenders.get(token));
	}

	@Override
	public NLPResult visit(Sentence sentence) {
		return new NLPResult("", new HashMap<String, GraphNode>());
	}
}