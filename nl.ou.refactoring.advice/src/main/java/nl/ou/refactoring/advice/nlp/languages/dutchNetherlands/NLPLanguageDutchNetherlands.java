package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Noun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTree;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * The Dutch language in the Netherlands (ISO designation "nl-NL") for Natural Language Processing.
 */
public final class NLPLanguageDutchNetherlands implements NLPLanguage {
	/**
	 * The default lookup tree for the declension of nouns in the Dutch (Netherlands) language.
	 */
	public static final Function<String, NounDeclensionLookupTree<GrammaticalNumber>> NOUN_DECLENSION_DEFAULT =
		(stem) -> new NounDeclensionLookupTree<GrammaticalNumber>(() -> stem, Nouns.declensionDefaultTree());
	
	/**
	 * The singleton instance of {@link NLPLanguageDutchNetherlands}.
	 */
	public static final NLPLanguageDutchNetherlands INSTANCE = new NLPLanguageDutchNetherlands();
	
	private final Map<Long, NounDeclensionLookupTree<GrammaticalNumber>> nounDeclension =
		new HashMap<Long, NounDeclensionLookupTree<GrammaticalNumber>>();
	private final Map<Long, GrammaticalGender> nounGenders =
		new HashMap<Long, GrammaticalGender>();
	private final Map<Long, VerbConjugationLookupTree<VerbAspect>> verbConjugations =
		new HashMap<Long, VerbConjugationLookupTree<VerbAspect>>();

	private NLPLanguageDutchNetherlands() {
		this.nounDeclension.putIfAbsent(Tokens.Nouns.Common.METHOD, NOUN_DECLENSION_DEFAULT.apply("methode"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.METHOD, GrammaticalGender.FEMININE);
		this.nounDeclension.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, NOUN_DECLENSION_DEFAULT.apply("microstap"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounDeclension.putIfAbsent(Tokens.Nouns.Common.REFACTORING, NOUN_DECLENSION_DEFAULT.apply("refactoring"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.REFACTORING, GrammaticalGender.MASCULINE);
	}

	@Override
	public Supplier<GrammaticalGender> getGenderSupplier(Noun noun) {
		return () -> this.nounGenders.get(noun.getToken());
	}

	@Override
	public NLPResult visit(Sentence sentence) {
		return new NLPResult("", new HashMap<String, GraphNode>());
	}
}