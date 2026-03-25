package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.nouns.Noun;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbModality;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTense;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;
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
	 * The lookup tree for Verb conjugation of the irregular verb "to be" in the English (Great Britain) language.
	 */
	public static final VerbConjugationLookupTree<VerbAspect> VERB_CONJUGATION_TO_BE =
		new VerbConjugationLookupTree<VerbAspect>(() -> "be", Verbs.conjugationToBeTree());
	
	/**
	 * The singleton instance of {@link NLPLanguageEnglishGreatBritain}.
	 */
	public static final NLPLanguageEnglishGreatBritain INSTANCE = new NLPLanguageEnglishGreatBritain();
	
	private final Map<Long, NounDeclensionLookupTree<GrammaticalNumber>> nounDeclensions =
		new HashMap<Long, NounDeclensionLookupTree<GrammaticalNumber>>();
	private final Map<Long, GrammaticalGender> nounGenders =
		new HashMap<Long, GrammaticalGender>();
	private final Map<Long, String> prepositions =
		new HashMap<Long, String>();
	private final Map<Long, VerbConjugationLookupTree<VerbAspect>> verbConjugations =
		new HashMap<Long, VerbConjugationLookupTree<VerbAspect>>();

	private NLPLanguageEnglishGreatBritain() {
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, NOUN_DECLENSION_DEFAULT.apply("class"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.METHOD, NOUN_DECLENSION_DEFAULT.apply("method"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.METHOD, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, NOUN_DECLENSION_DEFAULT.apply("microstep"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.REFACTORING, NOUN_DECLENSION_DEFAULT.apply("refactoring"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.REFACTORING, GrammaticalGender.MASCULINE);
		prepositions.putIfAbsent(Tokens.Prepositions.IN, "in");
		prepositions.putIfAbsent(Tokens.Prepositions.TO_DIRECTIONAL, "to");
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Auxiliary.BE, VERB_CONJUGATION_TO_BE);
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
		var result = new NLPResult("", new HashMap<String, GraphNode>());
		final var nounPhrase = sentence.getNounPhrase();
		if (nounPhrase.isPresent()) {
			result = result.merge(this.visit(nounPhrase.get()));
		}
		final var verbPhrase = sentence.getVerbPhrase();
		if (verbPhrase.isPresent()) {
			result = result.merge(this.visit(verbPhrase.get()), nounPhrase.isPresent() ? " " : "");
		}
		return result;
	}
	
	private NLPResult visit(NounPhrase nounPhrase) {
		final var noun = nounPhrase.getNoun();
		// TODO Inherit declension from Noun Phrase.
		final var nounDeclensionKey =
			new NounDeclensionKey(
				GrammaticalNumber.SINGULAR
			);
		final var nounString =
			this
				.nounDeclensions
				.get(noun.getToken())
				.lookup(nounDeclensionKey)
				.get();
		return new NLPResult(nounString, new HashMap<String, GraphNode>());
	}
	
	private NLPResult visit(PrepositionalPhrase prepositionalPhrase) {
		final var preposition = prepositionalPhrase.getPreposition();
		final var prepositionText = prepositions.get(preposition.getToken());
		
		var result = new NLPResult(prepositionText, new HashMap<String, GraphNode>());
		result = result.merge(this.visit(prepositionalPhrase.getNounPhrase()), " ");
		return result;
	}
	
	private NLPResult visit(VerbPhrase verbPhrase) {
		// TODO Check necessity of auxiliary verb if main verb requires it.
		// TODO Inherit conjugation from Sentence.
		final var resultTextBuilder = new StringBuilder();
		final var auxiliaryVerb = verbPhrase.getAuxiliaryVerb();
		if (auxiliaryVerb.isPresent()) {
			final var auxiliaryVerbConjugationKey =
				new VerbConjugationKey(
					GrammaticalPerson.THIRD,
					GrammaticalNumber.SINGULAR,
					VerbAspect.IMPERFECTIVE,
					VerbModality.INDICATIVE,
					VerbTense.PRESENT,
					VerbVoice.ACTIVE
				);
			resultTextBuilder.append(
				this
					.verbConjugations
					.get(auxiliaryVerb.get().getToken())
					.lookup(auxiliaryVerbConjugationKey)
					.get()
			);
			resultTextBuilder.append(" ");
		}

		final var verb = verbPhrase.getVerb();
		final var verbConjugationKey =
			new VerbConjugationKey(
				GrammaticalPerson.THIRD,
				GrammaticalNumber.SINGULAR,
				VerbAspect.PERFECTIVE,
				VerbModality.INDICATIVE,
				VerbTense.PRESENT,
				VerbVoice.ACTIVE
			);
		resultTextBuilder.append(
			this
				.verbConjugations
				.get(verb.getToken())
				.lookup(verbConjugationKey)
				.get()
		);
		
		var result = new NLPResult(resultTextBuilder.toString(), new HashMap<String, GraphNode>());
		
		final var prepositionalPhrase = verbPhrase.getPrepositionalPhrase();
		if (prepositionalPhrase.isPresent()) {
			result = result.merge(this.visit(prepositionalPhrase.get()), " ");
		}
		
		return result;
	}
}
