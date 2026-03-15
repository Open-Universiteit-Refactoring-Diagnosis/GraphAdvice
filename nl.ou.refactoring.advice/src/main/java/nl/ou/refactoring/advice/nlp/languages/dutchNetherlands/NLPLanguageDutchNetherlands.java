package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.HashMap;
import java.util.Optional;

import nl.ou.refactoring.advice.nlp.NLPResult;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalRegister;
import nl.ou.refactoring.advice.nlp.grammar.Sentence;
import nl.ou.refactoring.advice.nlp.grammar.determiners.PronounPossessive;
import nl.ou.refactoring.advice.nlp.languages.NLPLanguage;
import nl.ou.refactoring.advice.nlp.tokens.TokensLookupTreeNode;
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
	
	public final class Determiners {
		public final class PronounsPossessive {
			private static final TokensLookupTreeNode<PronounPossessiveKey, Void, GrammaticalPerson> LOOKUP =
				new TokensLookupTreeNode<PronounPossessiveKey, Void, GrammaticalPerson>(null, null);
			
			static {
				// First Person
				// First Person Singular
				final var mijnValue =
					new TokensLookupTreeNode<PronounPossessiveKey, String, Void>("mijn", _ -> "mijn");
				final var firstPersonSingular =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.SINGULAR, k -> k.number());
				firstPersonSingular.putIfAbsent(mijnValue);
				
				// First Person Plural
				final var onsValue =
					new TokensLookupTreeNode<PronounPossessiveKey, String, Void>("ons", _ -> "ons");
				final var onzeValue =
					new TokensLookupTreeNode<PronounPossessiveKey, String, Void>("onze", _ -> "onze");
				
				final var firstPersonPluralModifierNeuterSingular =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.SINGULAR, k -> k.numberModifier());
				firstPersonPluralModifierNeuterSingular.putIfAbsent(onsValue);

				final var firstPersonPluralModifierNeuterPlural =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalNumber, String>(GrammaticalNumber.PLURAL, k -> k.numberModifier());
				firstPersonPluralModifierNeuterPlural.putIfAbsent(onzeValue);
				final var firstPersonPluralModifierNeuter =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalGender, GrammaticalNumber>(GrammaticalGender.NEUTER, k -> k.genderModifier());
				firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterSingular);
				firstPersonPluralModifierNeuter.putIfAbsent(firstPersonPluralModifierNeuterPlural);
				final var firstPersonPluralModifierFeminine =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalGender, String>(GrammaticalGender.FEMININE, k -> k.genderModifier());
				firstPersonPluralModifierFeminine.putIfAbsent(onzeValue);
				final var firstPersonPluralModifierMasculine =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalGender, String>(GrammaticalGender.MASCULINE, k -> k.genderModifier());
				firstPersonPluralModifierMasculine.putIfAbsent(onzeValue);
				
				final var firstPersonPlural =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalNumber, GrammaticalGender>(GrammaticalNumber.PLURAL, k -> k.number());
				firstPersonPlural.putIfAbsent(firstPersonPluralModifierFeminine);
				firstPersonPlural.putIfAbsent(firstPersonPluralModifierMasculine);
				firstPersonPlural.putIfAbsent(firstPersonPluralModifierNeuter);
							
				final var firstPerson =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalPerson, GrammaticalNumber>(GrammaticalPerson.FIRST, k -> k.person());
				firstPerson.putIfAbsent(firstPersonSingular);
				firstPerson.putIfAbsent(firstPersonPlural);
				LOOKUP.putIfAbsent(firstPerson);
				
				// Second Person
				// Second Person Singular
				final var jouwValue =
					new TokensLookupTreeNode<PronounPossessiveKey, String, Void>("jouw", _ -> "jouw");
				final var secondPersonSingularCasual =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalRegister, String>(GrammaticalRegister.CASUAL, k -> k.register());
				secondPersonSingularCasual.putIfAbsent(jouwValue);
				final var secondPersonSingular =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalNumber, GrammaticalRegister>(GrammaticalNumber.SINGULAR, k -> k.number());
				secondPersonSingular.putIfAbsent(secondPersonSingularCasual);
				
				final var secondPerson =
					new TokensLookupTreeNode<PronounPossessiveKey, GrammaticalPerson, GrammaticalNumber>(GrammaticalPerson.SECOND, k -> k.person());
				secondPerson.putIfAbsent(secondPersonSingular);
				LOOKUP.putIfAbsent(secondPerson);
			}
			
			public static Optional<String> getString
			(
				PronounPossessive possessivePronoun,
				GrammaticalGender modifierGender,
				GrammaticalNumber modifierNumber,
				GrammaticalRegister modifierRegister
			) {
				final var person = possessivePronoun.getPerson();
				final var gender = possessivePronoun.getGender();
				final var number = possessivePronoun.getNumber();
				final var key =
					new PronounPossessiveKey
					(
						person,
						gender,
						number,
						modifierGender,
						modifierNumber,
						modifierRegister
					);
				Optional<TokensLookupTreeNode<PronounPossessiveKey, ?, ?>> nodeCurrent = Optional.of(LOOKUP);
				Optional<String> matchingValue = Optional.empty();
				do {
					final var node = nodeCurrent.get();
					final var nodeNext = node.getChildNext(key);
					if (nodeNext.isPresent()) {
						nodeCurrent = Optional.of(nodeNext.get());
					} else {
						if (String.class.isInstance(node.getValue())) {
							return Optional.of((String)node.getValue());
						}
						nodeCurrent = Optional.empty();
					}
					
				} while (nodeCurrent.isPresent());
				return matchingValue;
			}
		}
	}
}