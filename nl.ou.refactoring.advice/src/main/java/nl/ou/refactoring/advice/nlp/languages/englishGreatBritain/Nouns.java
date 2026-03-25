package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import java.util.function.BiFunction;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTreeNode;

class Nouns {
	private Nouns() { }
	
	static NounDeclensionLookupTreeNode<Void, GrammaticalNumber> declensionDefaultTree() {
		final var rootNode = new NounDeclensionLookupTreeNode<Void, GrammaticalNumber>(null, null);
		
		// Singular
		final var singularNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, BiFunction<String, NounDeclensionKey, String>>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		rootNode.putIfAbsent(singularNode);
		BiFunction<String, NounDeclensionKey, String> singularDeclension = (s, _) -> s;
		final var singularDeclensionNode =
			new NounDeclensionLookupTreeNode<BiFunction<String, NounDeclensionKey, String>, Void>(
				singularDeclension,
				_ -> singularDeclension
			);
		singularNode.putIfAbsent(singularDeclensionNode);
		
		// Plural
		final var pluralNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, BiFunction<String, NounDeclensionKey, String>>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		rootNode.putIfAbsent(pluralNode);
		BiFunction<String, NounDeclensionKey, String> pluralDeclension = (s, k) -> plural(s, k);
		final var pluralDeclensionNode =
			new NounDeclensionLookupTreeNode<BiFunction<String, NounDeclensionKey, String>, Void>(
				pluralDeclension,
				_ -> pluralDeclension
			);
		pluralNode.putIfAbsent(pluralDeclensionNode);
		
		return rootNode;
	}

	private static String plural(String stem, NounDeclensionKey key) {
		return stem + "s";
	}
}
