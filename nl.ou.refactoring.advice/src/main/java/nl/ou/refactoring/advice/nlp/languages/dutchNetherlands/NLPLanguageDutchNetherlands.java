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
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounPhrase;
import nl.ou.refactoring.advice.nlp.grammar.nouns.ReferenceNoun;
import nl.ou.refactoring.advice.nlp.grammar.prepositions.PrepositionalPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.AuxiliaryVerb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.Verb;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTree;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbPhrase;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;
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
	 * The default lookup tree for Verb conjugation in the Dutch (Netherlands) language.
	 */
	public static final Function<String, VerbConjugationLookupTree<GrammaticalNumber>> VERB_CONJUGATION_DEFAULT =
		(stem) -> new VerbConjugationLookupTree<GrammaticalNumber>(() -> stem, Verbs.conjugationDefaultTree());
	
	/**
	 * The singleton instance of {@link NLPLanguageDutchNetherlands}.
	 */
	public static final NLPLanguageDutchNetherlands INSTANCE = new NLPLanguageDutchNetherlands();
	
	private final Map<Long, NounDeclensionLookupTree<GrammaticalNumber>> nounDeclensions =
		new HashMap<Long, NounDeclensionLookupTree<GrammaticalNumber>>();
	private final Map<Long, GrammaticalGender> nounGenders =
		new HashMap<Long, GrammaticalGender>();
	private final Map<Long, String> prepositions =
		new HashMap<Long, String>();
	private final Map<Long, VerbConjugationLookupTree<GrammaticalNumber>> verbConjugations =
		new HashMap<Long, VerbConjugationLookupTree<GrammaticalNumber>>();

	private NLPLanguageDutchNetherlands() {
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, NOUN_DECLENSION_DEFAULT.apply("klasse"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.CLASS_OO_PROGRAMMING, GrammaticalGender.FEMININE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.METHOD, NOUN_DECLENSION_DEFAULT.apply("methode"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.METHOD, GrammaticalGender.FEMININE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, NOUN_DECLENSION_DEFAULT.apply("microstap"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.MICROSTEP, GrammaticalGender.MASCULINE);
		this.nounDeclensions.putIfAbsent(Tokens.Nouns.Common.REFACTORING, NOUN_DECLENSION_DEFAULT.apply("refactoring"));
		this.nounGenders.putIfAbsent(Tokens.Nouns.Common.REFACTORING, GrammaticalGender.MASCULINE);
		prepositions.putIfAbsent(Tokens.Prepositions.IN, "in");
		prepositions.putIfAbsent(Tokens.Prepositions.TO_TARGET_RECIPIENT, "aan");
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Auxiliary.BECOME, VERB_CONJUGATION_DEFAULT.apply("word"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.ADD, VERB_CONJUGATION_DEFAULT.apply("voeg"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.CAUSE, VERB_CONJUGATION_DEFAULT.apply("veroorzaak"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REFACTOR, VERB_CONJUGATION_DEFAULT.apply("refactor"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Lexical.REMOVE, VERB_CONJUGATION_DEFAULT.apply("verwijder"));
		this.verbConjugations.putIfAbsent(Tokens.Verbs.Linking.BECOME, VERB_CONJUGATION_DEFAULT.apply("word"));
	}

	@Override
	public Supplier<GrammaticalGender> getGenderSupplier(Noun noun) {
		return () -> this.nounGenders.get(noun.getToken());
	}
	
	@Override
	public String toString() {
		return "nl-NL";
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
		final var nounDeclensionKey = nounPhrase.getDeclension();
		
		final var references = new HashMap<String, GraphNode>();
		final var nounString = switch(noun) {
		case ReferenceNoun<?> referenceNoun -> {
			final var reference = referenceNoun.getReference();
			if (!GraphNode.class.isInstance(reference)) {
				yield reference.toString();
			}
			final var referenceKey = String.format("{%s}", ((GraphNode)referenceNoun.getReference()).getId().toString());
			references.putIfAbsent(referenceKey, (GraphNode)referenceNoun.getReference());
			yield referenceKey;
		}
		default ->
			this
				.nounDeclensions
				.get(noun.getToken())
				.lookup(nounDeclensionKey)
				.get();
		};
	
		return new NLPResult(nounString, references);
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
		final var resultTextBuilder = new StringBuilder();

		final var verb = verbPhrase.getVerb();
		verbPhrase = this.visit(verbPhrase, verb);
		final var verbAuxiliaryVerbs = verb.getAuxiliaryVerbs();
		final var verbConjugationKey = verbPhrase.getConjugation();
		for (final var auxiliaryVerb : verbAuxiliaryVerbs) {
			final var auxiliaryVerbConjugationKey = auxiliaryVerb.getConjugation();
			resultTextBuilder.append(
				this
					.verbConjugations
					.get(auxiliaryVerb.getToken())
					.lookup(auxiliaryVerbConjugationKey)
					.get()
			);
			resultTextBuilder.append(" ");
		}
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
	
	private VerbPhrase visit(VerbPhrase verbPhrase, Verb mainVerb) {
		final var mainVerbConjugation = verbPhrase.getConjugation();
		switch (mainVerbConjugation) {
			case VerbConjugationKey(var _, var _, var _, var _, var _, VerbVoice voice, var _): {
				switch (voice) {
					case VerbVoice.PASSIVE: {
						final var passiveBecomes = AuxiliaryVerb.setByToken(Tokens.Verbs.Auxiliary.BECOME, mainVerb);
						passiveBecomes.ifPresent(auxiliaryVerb -> {
							auxiliaryVerb.setConjugation(mainVerbConjugation.withVoice(VerbVoice.ACTIVE));
						});
					}
					default: {
						break;
					}
				}
			}
			default: {
				return verbPhrase;
			}
		}
	}
}