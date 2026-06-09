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
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.AuxiliaryVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.Verb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;
import nl.ou.refactoring.advice.nodes.GraphNode;

/**
 * The English language in Great Britain (ISO designation "en-GB") for Natural
 * Language Processing.
 */
public final class NLPLanguageEnglishGreatBritain implements NLPLanguage {
	/**
	 * Creates a default lookup tree for Noun declension in the English (Great
	 * Britain) language.
	 */
	public static final Function<String, NounDeclensionLookupTree<GrammaticalNumber>> NOUN_DECLENSION_DEFAULT =
			(stem) -> new NounDeclensionLookupTree<GrammaticalNumber>(() -> stem, Nouns.declensionDefaultTree());

	/**
	 * The default lookup tree for Verb conjugation in the English (Great Britain)
	 * language.
	 */
	public static final Function<String, VerbConjugationLookupTree<GrammaticalPerson>> VERB_CONJUGATION_DEFAULT =
			(stem) -> new VerbConjugationLookupTree<GrammaticalPerson>(() -> stem, Verbs.conjugationDefaultTree());

	/**
	 * The lookup tree for Verb conjugation of the irregular verb "to be" in the
	 * English (Great Britain) language.
	 */
	public static final VerbConjugationLookupTree<VerbAspect> VERB_CONJUGATION_TO_BE =
			new VerbConjugationLookupTree<VerbAspect>(() -> "be", Verbs.conjugationToBeTree());

	/**
	 * The singleton instance of {@link NLPLanguageEnglishGreatBritain}.
	 */
	public static final NLPLanguageEnglishGreatBritain INSTANCE = new NLPLanguageEnglishGreatBritain();

	private final Map<Long, NounDeclensionLookupTree<GrammaticalNumber>> nounDeclensions =
			new HashMap<Long, NounDeclensionLookupTree<GrammaticalNumber>>();
	private final Map<Long, GrammaticalGender> nounGenders = new HashMap<Long, GrammaticalGender>();
	private final Map<Long, String> prepositions = new HashMap<Long, String>();
	private final Map<Long, VerbConjugationLookupTree<?>> verbConjugations =
			new HashMap<Long, VerbConjugationLookupTree<?>>();

	private NLPLanguageEnglishGreatBritain() {
		this.nounDeclensions
				.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, NOUN_DECLENSION_DEFAULT.apply("class"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, GrammaticalGender.MASCULINE);
		this.nounDeclensions
				.putIfAbsent(Tokens.Nouns.Common.CONFLICT_INCOMPATIBILITY, NOUN_DECLENSION_DEFAULT.apply("conflict"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.CONFLICT_INCOMPATIBILITY, GrammaticalGender.MASCULINE);
		this.nounDeclensions
				.putIfAbsent(Tokens.Nouns.Common.FIELD_OO_PROGRAMMING, NOUN_DECLENSION_DEFAULT.apply("field"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.FIELD_OO_PROGRAMMING, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.METHOD, NOUN_DECLENSION_DEFAULT.apply("method"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.METHOD, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, NOUN_DECLENSION_DEFAULT.apply("microstep"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Verbal.REFACTORING, NOUN_DECLENSION_DEFAULT.apply("refactoring"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Verbal.REFACTORING, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Verbal.RENAMING, NOUN_DECLENSION_DEFAULT.apply("renaming"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Verbal.RENAMING, GrammaticalGender.MASCULINE);
		prepositions.putIfAbsent(Tokens.Prepositions.FROM_REMOVAL_SEPARATION, "from");
		prepositions.putIfAbsent(Tokens.Prepositions.IN, "in");
		prepositions.putIfAbsent(Tokens.Prepositions.OF_AGENCY_SUBJECTIVE_GENITIVE, "of");
		prepositions.putIfAbsent(Tokens.Prepositions.TO_TARGET_RECIPIENT, "to");
		prepositions.putIfAbsent(Tokens.Prepositions.WITH_AGAINST, "with");
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Auxiliary.BE, VERB_CONJUGATION_TO_BE);
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.ADD, VERB_CONJUGATION_DEFAULT.apply("add"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.CAUSE, VERB_CONJUGATION_DEFAULT.apply("cause"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.CONFLICT, VERB_CONJUGATION_DEFAULT.apply("conflict"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REFACTOR, VERB_CONJUGATION_DEFAULT.apply("refactor"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REMOVE, VERB_CONJUGATION_DEFAULT.apply("remove"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Linking.BE, VERB_CONJUGATION_TO_BE);
	}

	@Override
	public Supplier<GrammaticalGender> getGenderSupplier(Noun noun) {
		return () -> this.nounGenders.get(noun.getToken());
	}

	@Override
	public String toString() {
		return "en-GB";
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
		return result.capitaliseFirstLetter().appendPeriod();
	}

	private NLPResult visit(NounPhrase nounPhrase) {
		final var noun = nounPhrase.getNoun();
		final var nounDeclensionKey = nounPhrase.getDeclension();

		final var references = new HashMap<String, GraphNode>();
		final var nounString = switch (noun) {
			case ReferenceNoun<?> referenceNoun -> {
				final var reference = referenceNoun.getReference();
				if (!GraphNode.class.isInstance(reference)) {
					yield reference.toString();
				}
				final var referenceKey = NLPResult.extractReferenceString(((GraphNode) referenceNoun.getReference()));
				references.putIfAbsent(referenceKey, (GraphNode) referenceNoun.getReference());
				yield referenceKey;
			}
			default -> this.nounDeclensions.get(noun.getToken()).lookup(nounDeclensionKey).get();
		};

		var result = new NLPResult(nounString, references);
		return nounPhrase.getPrepositionalPhrase()
				.map((phrase) -> result.merge(this.visit(phrase), " "))
				.orElse(result);
	}

	private NLPResult visit(PrepositionalPhrase prepositionalPhrase) {
		final var preposition = prepositionalPhrase.getPreposition();
		final var prepositionText = prepositions.get(preposition.getToken());

		var result = new NLPResult(prepositionText, new HashMap<String, GraphNode>());
		result = result.merge(this.visit(prepositionalPhrase.getNounPhrase()), " ");
		return result;
	}

	private NLPResult visit(VerbPhrase verbPhrase) {
		final var resultTextBuilder = new StringBuilder();

		final var verb = verbPhrase.getVerb(); // Linking Verb or Lexical Verb
		verbPhrase = this.visit(verbPhrase, verb);
		final var verbAuxiliaryVerbs = verb.getAuxiliaryVerbs();
		final var verbConjugationKey = verbPhrase.getConjugation();
		for (final var auxiliaryVerb : verbAuxiliaryVerbs) {
			final var auxiliaryVerbConjugationKey = auxiliaryVerb.getConjugation();
			resultTextBuilder.append(
					this.verbConjugations.get(auxiliaryVerb.getToken()).lookup(auxiliaryVerbConjugationKey).get());
			resultTextBuilder.append(" ");
		}
		resultTextBuilder.append(this.verbConjugations.get(verb.getToken()).lookup(verbConjugationKey).get());

		var result = new NLPResult(resultTextBuilder.toString(), new HashMap<String, GraphNode>());

		final var nounPhrase = verbPhrase.getNounPhrase();
		if (nounPhrase.isPresent()) {
			result = result.merge(this.visit(nounPhrase.get()), " ");
		}

		final var prepositionalPhrase = verbPhrase.getPrepositionalPhrase();
		if (prepositionalPhrase.isPresent()) {
			result = result.merge(this.visit(prepositionalPhrase.get()), " ");
		}

		return result;
	}

	private VerbPhrase visit(VerbPhrase verbPhrase, Verb mainVerb) {
		final var mainVerbConjugation = verbPhrase.getConjugation();
		switch (mainVerbConjugation) {
			case VerbConjugationKey(var _, var _, var _, var _, var _, VerbVoice voice, var _): {
				switch (voice) {
					case VerbVoice.PASSIVE: {
						final var passiveIs = AuxiliaryVerb.setByToken(Tokens.Verbs.Auxiliary.BE, mainVerb);
						passiveIs.ifPresent(auxiliaryVerb -> {
							auxiliaryVerb.setConjugation(mainVerbConjugation.withVoice(VerbVoice.ACTIVE));
						});
					}
					default: {
						break;
					}
				}
				return verbPhrase;
			}
			default: {
				return verbPhrase;
			}
		}
	}
}
