package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.function.BiFunction;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalPerson;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbAspect;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationKey;
import nl.ou.refactoring.advice.nlp.grammar.verbs.VerbConjugationLookupTreeNode;

class Verbs {
	private Verbs() { }

	static VerbConjugationLookupTreeNode<Void, VerbAspect> conjugationDefaultTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, VerbAspect>(null, null);
		
		// Imperfective Singular
		final var imperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, GrammaticalNumber>(
				VerbAspect.IMPERFECTIVE,
				k -> k.aspect()
			);
		rootNode.putIfAbsent(imperfectiveNode);
		final var imperfectiveSingularNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, GrammaticalPerson>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent(imperfectiveSingularNode);
		final var imperfectiveSingularThirdPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, BiFunction<String, VerbConjugationKey, String>>(
				GrammaticalPerson.THIRD,
				k -> k.person()
			);
		imperfectiveSingularNode.putIfAbsent(imperfectiveSingularThirdPersonNode);
		BiFunction<String, VerbConjugationKey, String> imperfectiveSingularThirdPersonConjugation =
			(s, _) -> s + "s"; // add -> adds
		final var imperfectiveSingularThirdPersonConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				imperfectiveSingularThirdPersonConjugation,
				_ -> imperfectiveSingularThirdPersonConjugation
			);
		imperfectiveSingularThirdPersonNode.putIfAbsent(imperfectiveSingularThirdPersonConjugationNode);
		
		// Imperfective Plural
		final var imperfectivePluralNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, BiFunction<String, VerbConjugationKey, String>>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent(imperfectivePluralNode);
		BiFunction<String, VerbConjugationKey, String> imperfectivePluralConjugation =
			(s, _) -> s; // add -> add
		final var imperfectivePluralConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				imperfectivePluralConjugation,
				_ -> imperfectivePluralConjugation
			);
		imperfectivePluralNode.putIfAbsent(imperfectivePluralConjugationNode);
		
		// Perfective
		final var perfectiveNode = new VerbConjugationLookupTreeNode<VerbAspect, BiFunction<String, VerbConjugationKey, String>>(
			VerbAspect.PERFECTIVE,
			k -> k.aspect()
		);
		rootNode.putIfAbsent(perfectiveNode);
		BiFunction<String, VerbConjugationKey, String> perfectiveConjugation =
			(s, k) -> perfective(s, k); // add -> added
		final var perfectiveConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				perfectiveConjugation,
				_ -> perfectiveConjugation
			);
		perfectiveNode.putIfAbsent(perfectiveConjugationNode);
		
		return rootNode;
	}
	
	static VerbConjugationLookupTreeNode<Void, VerbAspect> conjugationToBeTree() {
		final var rootNode = new VerbConjugationLookupTreeNode<Void, VerbAspect>(null, null);
		
		// Imperfective Singular
		final var imperfectiveNode =
			new VerbConjugationLookupTreeNode<VerbAspect, GrammaticalNumber>(
				VerbAspect.IMPERFECTIVE,
				k -> k.aspect()
			);
		rootNode.putIfAbsent(imperfectiveNode);
		final var imperfectiveSingularNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, GrammaticalPerson>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent(imperfectiveSingularNode);
		final var imperfectiveSingularThirdPersonNode =
			new VerbConjugationLookupTreeNode<GrammaticalPerson, BiFunction<String, VerbConjugationKey, String>>(
				GrammaticalPerson.THIRD,
				k -> k.person()
			);
		imperfectiveSingularNode.putIfAbsent(imperfectiveSingularThirdPersonNode);
		BiFunction<String, VerbConjugationKey, String> imperfectiveSingularThirdPersonConjugation =
			(_, _) -> "is"; // to be -> is
		final var imperfectiveSingularThirdPersonConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				imperfectiveSingularThirdPersonConjugation,
				_ -> imperfectiveSingularThirdPersonConjugation
			);
		imperfectiveSingularThirdPersonNode.putIfAbsent(imperfectiveSingularThirdPersonConjugationNode);
		
		// Imperfective Plural
		final var imperfectivePluralNode =
			new VerbConjugationLookupTreeNode<GrammaticalNumber, BiFunction<String, VerbConjugationKey, String>>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		imperfectiveNode.putIfAbsent(imperfectivePluralNode);
		BiFunction<String, VerbConjugationKey, String> imperfectivePluralConjugation =
			(_, _) -> "are"; // to be -> are
		final var imperfectivePluralConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				imperfectivePluralConjugation,
				_ -> imperfectivePluralConjugation
			);
		imperfectivePluralNode.putIfAbsent(imperfectivePluralConjugationNode);
		
		// Perfective
		final var perfectiveNode = new VerbConjugationLookupTreeNode<VerbAspect, BiFunction<String, VerbConjugationKey, String>>(
			VerbAspect.PERFECTIVE,
			k -> k.aspect()
		);
		rootNode.putIfAbsent(perfectiveNode);
		BiFunction<String, VerbConjugationKey, String> perfectiveConjugation =
			(_, _) -> "been"; // to be -> been
		final var perfectiveConjugationNode =
			new VerbConjugationLookupTreeNode<BiFunction<String, VerbConjugationKey, String>, Void>(
				perfectiveConjugation,
				_ -> perfectiveConjugation
			);
		perfectiveNode.putIfAbsent(perfectiveConjugationNode);
		
		return rootNode;
	}
	
	private static String perfective(String stem, VerbConjugationKey key) {
		if (stem.length() > 2 &&
			stem.charAt(stem.length() - 2) == 'e') {
			stem += "d";
		} else {
			stem += "ed";
		}
		return stem;
	}
}
