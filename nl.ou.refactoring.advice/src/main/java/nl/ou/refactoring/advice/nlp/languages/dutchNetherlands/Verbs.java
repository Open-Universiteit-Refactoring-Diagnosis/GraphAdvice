package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.Set;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationProducer;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbVoice;

class Verbs {
	private Verbs() { }
	
	private static final VerbConjugationProducer SINGULAR_THIRD_PERSON_ACTIVE_CONJUGATION =
		(s, _) -> s + "t";
	private static final VerbConjugationProducer PERFECTIVE_CONJUGATION =
		(s, _) -> (s.startsWith("ver") ? "" : "ge") + s + (s.chars().skip(s.length() - 1).anyMatch(c -> "kfschp".indexOf(c) >= 0) ? "t" : "d");
		
	private static final VerbConjugationLookupTreeNode<VerbConjugationProducer, Void> SINGULAR_THIRD_PERSON_ACTIVE_CONJUGATION_NODE =
		new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
			Set.of(SINGULAR_THIRD_PERSON_ACTIVE_CONJUGATION),
			_ -> SINGULAR_THIRD_PERSON_ACTIVE_CONJUGATION
		);
	private static final VerbConjugationLookupTreeNode<VerbConjugationProducer, Void> SINGULAR_THIRD_PERSON_PASSIVE_CONJUGATION_NODE =
		new VerbConjugationLookupTreeNode<VerbConjugationProducer, Void>(
			Set.of(PERFECTIVE_CONJUGATION),
			_ -> PERFECTIVE_CONJUGATION
		);

	static VerbConjugationLookupTreeNode<Void, GrammaticalNumber> conjugationDefaultTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, GrammaticalNumber>(Set.of(), null);
		
		// Singular
		final var singularNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, GrammaticalPerson>(
				Set.of(GrammaticalNumber.SINGULAR),
				k -> k.number()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalNumber, ?, ?>)singularNode);
		
		// Third Person (Singular)
		final var singularThirdPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, VerbVoice>(
				Set.of(GrammaticalPerson.THIRD),
				k -> k.person()
			);
		singularNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, GrammaticalPerson, ?, ?>)singularThirdPersonNode);
		
		// Active (Singular Third Person)
		final var singularThirdPersonActiveNode =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.ACTIVE, VerbVoice.MIDDLE, VerbVoice.REFLEXIVE),
				k -> k.voice()
			);
		singularThirdPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)singularThirdPersonActiveNode);	
		singularThirdPersonActiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)SINGULAR_THIRD_PERSON_ACTIVE_CONJUGATION_NODE);		
		
		// Passive (Singular Third Person)
		final var singularThirdPersonPassiveNode =
			new VerbConjugationLookupTreeNode<VerbVoice, VerbConjugationProducer>(
				Set.of(VerbVoice.PASSIVE),
				k -> k.voice()
			);
		singularThirdPersonNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbVoice, ?, ?>)singularThirdPersonPassiveNode);
		singularThirdPersonPassiveNode.putIfAbsent((LookupStemTreeNode<VerbConjugationKey, VerbConjugationProducer, ?, ?>)SINGULAR_THIRD_PERSON_PASSIVE_CONJUGATION_NODE);
		
		return rootNode;
	}
}
