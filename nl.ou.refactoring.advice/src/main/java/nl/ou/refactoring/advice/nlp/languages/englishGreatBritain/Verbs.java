package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.Set;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationProducer;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbModality;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbTense;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;

class Verbs {
	private Verbs() { }
	
	private static String baseFormConjugation(String stem, VerbConjugationKey conjugation) {
		return stem;
	}
	
	private static String pastPerfectiveOrPassiveConjugation(String stem, VerbConjugationKey conjugation) {
		if (stem.length() > 0 &&
			stem.charAt(stem.length() - 1) == 'e') {
			stem += "d";
		} else {
			stem += "ed";
		}
		return stem;
	}
	
	private static final VerbConjugationLookupTreeNode<VerbConjugationProducer, Void> BASE_FORM_CONJUGATION_NODE =
		new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
			Set.of(Verbs::baseFormConjugation),
			_ -> Verbs::baseFormConjugation
		);
	
	private static final VerbConjugationLookupTreeNode<VerbConjugationProducer, Void> PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE =
		new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
			Set.of(Verbs::pastPerfectiveOrPassiveConjugation),
			_ -> Verbs::pastPerfectiveOrPassiveConjugation
		);
	
	static VerbConjugationLookupTreeNode<Void, GrammaticalPerson> conjugationDefaultTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, GrammaticalPerson>(Set.of(), null);
		
		// First and Second Person (unmarked)
		final var unmarkedPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, VerbModality>(
				Set.of(GrammaticalPerson.FIRST, GrammaticalPerson.SECOND),
				k -> k.person()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalPerson, ?, ?>)unmarkedPersonNode);
		
		// First/Second: MODALITY
		final var unmarkedPersonSubjunctiveImperativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbConjugationProducer>(
				Set.of(VerbModality.SUBJUNCTIVE, VerbModality.IMPERATIVE),
				k -> k.modality()
			);
		unmarkedPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)unmarkedPersonSubjunctiveImperativeNode);
		unmarkedPersonSubjunctiveImperativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)BASE_FORM_CONJUGATION_NODE);
		final var unmarkedPersonIndicativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbTense>(
				Set.of(VerbModality.CONDITIONAL, VerbModality.INDICATIVE, VerbModality.OPTATIVE),
				k -> k.modality()
			);
		unmarkedPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)unmarkedPersonIndicativeNode);
		
		// First/Second: TENSE
		final var unmarkedPersonIndicativePresentNode =
			new VerbConjugationLookupTreeNode<VerbTense, VerbAspect>(
				Set.of(VerbTense.PRESENT),
				k -> k.tense()
			);
		unmarkedPersonIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbTense, ?, ?>)unmarkedPersonIndicativePresentNode);
		final var unmarkedPersonIndicativePastNode =
			new VerbConjugationLookupTreeNode<VerbTense, VerbAspect>(
				Set.of(VerbTense.PAST),
				k -> k.tense()
			);
		unmarkedPersonIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbTense, ?, ?>)unmarkedPersonIndicativePastNode);
		
		// First/Second: ASPECT (present)
		final var unmarkedPersonIndicativePresentImperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbVoice>(
				Set.of(VerbAspect.IMPERFECTIVE),
				k -> k.aspect()
			);
		unmarkedPersonIndicativePresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)unmarkedPersonIndicativePresentImperfectiveNode);
		final var unmarkedPersonIndicativePresentProgressiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbVoice>(
				Set.of(VerbAspect.PROGRESSIVE),
				k -> k.aspect()
			);
		unmarkedPersonIndicativePresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)unmarkedPersonIndicativePresentProgressiveNode);
		final var unmarkedPersonIndicativePresentPerfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbConjugationProducer>(
				Set.of(VerbAspect.PERFECTIVE),
				k -> k.aspect()
			);
		unmarkedPersonIndicativePresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)unmarkedPersonIndicativePresentPerfectiveNode);
		unmarkedPersonIndicativePresentPerfectiveNode.putIfAbsent(PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE);
		
		// First/Second: ASPECT (past)
		final var unmarkedPersonIndicativePastImperfectivePerfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbConjugationProducer>(
				Set.of(VerbAspect.IMPERFECTIVE, VerbAspect.PERFECTIVE),
				k -> k.aspect()
			);
		unmarkedPersonIndicativePastNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)unmarkedPersonIndicativePastImperfectivePerfectiveNode);
		unmarkedPersonIndicativePastImperfectivePerfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE);
		
		// First/Second: VOICE (present imperfective)
		final var unmarkedPersonIndicativePresentImperfectiveActiveNode =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.ACTIVE, VerbVoice.MIDDLE, VerbVoice.REFLEXIVE),
				k -> k.voice()
			);
		unmarkedPersonIndicativePresentImperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)unmarkedPersonIndicativePresentImperfectiveActiveNode);
		unmarkedPersonIndicativePresentImperfectiveActiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)BASE_FORM_CONJUGATION_NODE);
		final var unmarkedPersonIndicativePresentImperfectivePassiveNode =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.PASSIVE),
				k -> k.voice()
			);
		unmarkedPersonIndicativePresentImperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)unmarkedPersonIndicativePresentImperfectivePassiveNode);
		unmarkedPersonIndicativePresentImperfectivePassiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE);
		
		// Third Person
		final var thirdPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, GrammaticalNumber>(
				Set.of(GrammaticalPerson.THIRD),
				k -> k.person()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalPerson, ?, ?>)thirdPersonNode);
		
		// Third Person: NUMBER
		final var thirdPersonSingularNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, VerbTense>(
				Set.of(GrammaticalNumber.SINGULAR),
				k -> k.number()
			);
		thirdPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalNumber, ?, ?>)thirdPersonSingularNode);
		final var thirdPersonPluralNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, VerbTense>(
				Set.of(GrammaticalNumber.PLURAL),
				k -> k.number()
			);
		thirdPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalNumber, ?, ?>)thirdPersonPluralNode);
		
		// Third Person: TENSE (singular)
		final var thirdPersonSingularPresentNode =
			new VerbConjugationLookupTreeNode<VerbTense, VerbAspect>(
				Set.of(VerbTense.PRESENT),
				k -> k.tense()
			);
		thirdPersonSingularNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbTense, ?, ?>)thirdPersonSingularPresentNode);
		
		// Third Person: ASPECT (singular present)
		final var thirdPersonSingularPresentImperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbModality>(
				Set.of(VerbAspect.IMPERFECTIVE),
				k -> k.aspect()
			);
		thirdPersonSingularPresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)thirdPersonSingularPresentImperfectiveNode);
		final var thirdPersonSingularPresentPerfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbModality>(
				Set.of(VerbAspect.PERFECTIVE),
				k -> k.aspect()
			);
		thirdPersonSingularPresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)thirdPersonSingularPresentPerfectiveNode);
		
		// Third Person: MODALITY (singular present imperfective)
		final var thirdPersonSingularPresentImperfectiveIndicativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbVoice>(
				Set.of(VerbModality.INDICATIVE),
				k -> k.modality()
			);
		thirdPersonSingularPresentImperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)thirdPersonSingularPresentImperfectiveIndicativeNode);
		
		// Third Person: MODALITY (singular present perfective)
		final var thirdPersonSingularPresentPerfectiveIndicativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbConjugationProducer>(
				Set.of(VerbModality.INDICATIVE),
				k -> k.modality()
			);
		thirdPersonSingularPresentPerfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)thirdPersonSingularPresentPerfectiveIndicativeNode);
		thirdPersonSingularPresentPerfectiveIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE);
		
		// Third Person: VOICE (singular present imperfective indicative)
		final var thirdPersonSingularPresentImperfectiveIndicativeActive =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.ACTIVE),
				k -> k.voice()
			);
		thirdPersonSingularPresentImperfectiveIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)thirdPersonSingularPresentImperfectiveIndicativeActive);
		final VerbConjugationProducer thirdPersonSingularPresentImperfectiveIndicativeActiveConjugation =
			(s, _) -> s + "s"; // to talk » he [talks]
		final var thirdPersonSingularPresentImperfectiveIndicativeActiveConjugationNode =
			new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
				Set.of(thirdPersonSingularPresentImperfectiveIndicativeActiveConjugation),
				_ -> thirdPersonSingularPresentImperfectiveIndicativeActiveConjugation
			);
		thirdPersonSingularPresentImperfectiveIndicativeActive.putIfAbsent(
			(LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)thirdPersonSingularPresentImperfectiveIndicativeActiveConjugationNode
		);
		final var thirdPersonSingularPresentImperfectiveIndicativePassive =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.PASSIVE),
				k -> k.voice()
			);
		thirdPersonSingularPresentImperfectiveIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)thirdPersonSingularPresentImperfectiveIndicativePassive);
		final VerbConjugationProducer thirdPersonSingularPresentImperfectiveIndicativePassiveConjugation =
			(s, k) -> pastPerfectiveOrPassiveConjugation(s, k); // to add » has been [added], to add » is [added]
		final var thirdPersonSingularPresentImperfectiveIndicativePassiveConjugationNode =
			new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
				Set.of(thirdPersonSingularPresentImperfectiveIndicativePassiveConjugation),
				_ -> thirdPersonSingularPresentImperfectiveIndicativePassiveConjugation
			);
		thirdPersonSingularPresentImperfectiveIndicativePassive.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)thirdPersonSingularPresentImperfectiveIndicativePassiveConjugationNode);
		
		// Third Person: TENSE (plural)
		final var thirdPersonPluralPresentNode =
			new VerbConjugationLookupTreeNode<VerbTense, VerbAspect>(
				Set.of(VerbTense.PRESENT),
				k -> k.tense()
			);
		thirdPersonPluralNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbTense, ?, ?>)thirdPersonPluralPresentNode);
		
		// Third Person: ASPECT (plural present)
		final var thirdPersonPluralPresentImperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbModality>(
				Set.of(VerbAspect.IMPERFECTIVE),
				k -> k.aspect()
			);
		thirdPersonPluralPresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)thirdPersonPluralPresentImperfectiveNode);
		final var thirdPersonPluralPresentPerfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, VerbModality>(
				Set.of(VerbAspect.PERFECTIVE),
				k -> k.aspect()
			);
		thirdPersonPluralPresentNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)thirdPersonPluralPresentPerfectiveNode);
		
		// Third Person: MODALITY (plural present imperfective)
		final var thirdPersonPluralPresentImperfectiveIndicativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbVoice>(
				Set.of(VerbModality.INDICATIVE),
				k -> k.modality()
			);
		thirdPersonPluralPresentImperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)thirdPersonPluralPresentImperfectiveIndicativeNode);
		
		// Third Person: MODALITY (plural present perfective)
		final var thirdPersonPluralPresentPerfectiveIndicativeNode =
			new VerbConjugationLookupTreeNode<VerbModality, VerbConjugationProducer>(
				Set.of(VerbModality.INDICATIVE),
				k -> k.modality()
			);
		thirdPersonPluralPresentImperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbModality, ?, ?>)thirdPersonPluralPresentPerfectiveIndicativeNode);
		thirdPersonPluralPresentPerfectiveIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)PAST_PERFECTIVE_PASSIVE_CONJUGATION_NODE);
		
		// Third Person: VOICE (plural present imperfective indicative)
		final var thirdPersonPluralPresentImperfectiveIndicativeActiveNode =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.ACTIVE),
				k -> k.voice()
			);
		thirdPersonPluralPresentImperfectiveIndicativeNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)thirdPersonPluralPresentImperfectiveIndicativeActiveNode);
		thirdPersonPluralPresentImperfectiveIndicativeActiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)BASE_FORM_CONJUGATION_NODE);
		
		return rootNode;
	}
	
	static VerbConjugationLookupTreeNode<Void, VerbAspect> conjugationToBeTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, VerbAspect>(Set.of(), null);
		
		// Imperfective Singular
		final var imperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, GrammaticalNumber>(
				Set.of(VerbAspect.IMPERFECTIVE),
				k -> k.aspect()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)imperfectiveNode);
		final var imperfectiveSingularNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, GrammaticalPerson>(
				Set.of(GrammaticalNumber.SINGULAR),
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalNumber, ?, ?>)imperfectiveSingularNode);
		final var imperfectiveSingularThirdPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, VerbConjugationProducer>(
				Set.of(GrammaticalPerson.THIRD),
				k -> k.person()
			);
		imperfectiveSingularNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalPerson, ?, ?>)imperfectiveSingularThirdPersonNode);
		VerbConjugationProducer imperfectiveSingularThirdPersonConjugation =
			(_, _) -> "is"; // to be -> is
		final var imperfectiveSingularThirdPersonConjugationNode =
			new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
				Set.of(imperfectiveSingularThirdPersonConjugation),
				_ -> imperfectiveSingularThirdPersonConjugation
			);
		imperfectiveSingularThirdPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)imperfectiveSingularThirdPersonConjugationNode);
		
		// Imperfective Plural
		final var imperfectivePluralNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, VerbConjugationProducer>(
				Set.of(GrammaticalNumber.PLURAL),
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalNumber, ?, ?>)imperfectivePluralNode);
		VerbConjugationProducer imperfectivePluralConjugation =
			(_, _) -> "are"; // to be -> are
		final var imperfectivePluralConjugationNode =
			new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
				Set.of(imperfectivePluralConjugation),
				_ -> imperfectivePluralConjugation
			);
		imperfectivePluralNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)imperfectivePluralConjugationNode);
		
		// Perfective
		final var perfectiveNode = new VerbConjugationLookupTreeNode<VerbAspect, VerbConjugationProducer>(
			Set.of(VerbAspect.PERFECTIVE),
			k -> k.aspect()
		);
		rootNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbAspect, ?, ?>)perfectiveNode);
		VerbConjugationProducer perfectiveConjugation =
			(_, _) -> "been"; // to be -> been
		final var perfectiveConjugationNode =
			new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
				Set.of(perfectiveConjugation),
				_ -> perfectiveConjugation
			);
		perfectiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)perfectiveConjugationNode);
		
		return rootNode;
	}
}
