package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

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
 * The English language in Great Britain (ISO designation "en-GB") for Natural Language Processing.
 */
public final class NLPLanguageEnglishGreatBritain implements NLPLanguage {
	/**
	 * The default lookup tree for Noun declension in the English (Great Britain) language.
	 */
	public static final Function<String, NounDeclensionLookupTree<GrammaticalNumber>> NOUN_DECLENSION_DEFAULT =
		(stem) -> new NounDeclensionLookupTree<GrammaticalNumber>(() -> stem, Nouns.declensionDefaultTree());
	
	/**
	 * The default lookup tree for Verb conjugation in the English (Great Britain) language.
	 */
	public static final Function<String, VerbConjugationLookupTree<VerbAspect>> VERB_CONJUGATION_DEFAULT =
		(stem) -> new VerbConjugationLookupTree<VerbAspect>(() -> stem, Verbs.conjugationDefaultTree());
	
	/**
	 * The singleton instance of {@link NLPLanguageEnglishGreatBritain}.
	 */
	public static final NLPLanguageEnglishGreatBritain INSTANCE = new NLPLanguageEnglishGreatBritain();
	
	private final Map<Long, NounDeclensionLookupTree<GrammaticalNumber>> nounDeclensions =
		new HashMap<Long, NounDeclensionLookupTree<GrammaticalNumber>>();
	private final Map<Long, GrammaticalGender> nounGenders =
		new HashMap<Long, GrammaticalGender>();
	private final Map<Long, VerbConjugationLookupTree<VerbAspect>> verbConjugations =
		new HashMap<Long, VerbConjugationLookupTree<VerbAspect>>();

	private NLPLanguageEnglishGreatBritain() {
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.METHOD, NOUN_DECLENSION_DEFAULT.apply("method"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.METHOD, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, NOUN_DECLENSION_DEFAULT.apply("microstep"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.REFACTORING, NOUN_DECLENSION_DEFAULT.apply("refactoring"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.REFACTORING, GrammaticalGender.MASCULINE);
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.ADD, VERB_CONJUGATION_DEFAULT.apply("add"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.CAUSE, VERB_CONJUGATION_DEFAULT.apply("cause"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REFACTOR, VERB_CONJUGATION_DEFAULT.apply("refactor"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REMOVE, VERB_CONJUGATION_DEFAULT.apply("remove"));
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
