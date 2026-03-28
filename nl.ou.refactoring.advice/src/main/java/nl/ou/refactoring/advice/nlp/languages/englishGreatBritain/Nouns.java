package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain;

import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionProducer;

class Nouns {
	private Nouns() { }
	
	static NounDeclensionLookupTreeNode<Void, GrammaticalNumber> declensionDefaultTree() {
		final var rootNode = new NounDeclensionLookupTreeNode<Void, GrammaticalNumber>(null, null);
		
		// Singular
		final var singularNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, NounDeclensionProducer>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		rootNode.putIfAbsent(singularNode);
		NounDeclensionProducer singularDeclension = (s, _) -> s;
		final var singularDeclensionNode =
			new NounDeclensionLookupTreeNode<NounDeclensionProducer, Void>(
				singularDeclension,
				_ -> singularDeclension
			);
		singularNode.putIfAbsent(singularDeclensionNode);
		
		// Plural
		final var pluralNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, NounDeclensionProducer>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		rootNode.putIfAbsent(pluralNode);
		NounDeclensionProducer pluralDeclension = (s, k) -> plural(s, k);
		final var pluralDeclensionNode =
			new NounDeclensionLookupTreeNode<NounDeclensionProducer, Void>(
				pluralDeclension,
				_ -> pluralDeclension
			);
		pluralNode.putIfAbsent(pluralDeclensionNode);
		
		return rootNode;
	}
	
	static NounDeclensionLookupTreeNode<Void, NounDeclensionProducer> declensionReferenceTree() {
		final var rootNode = new NounDeclensionLookupTreeNode<Void, NounDeclensionProducer>(null, null);
		
		NounDeclensionProducer declension = (s, _) -> String.format("{%s}", s);
		final var declensionNode = new NounDeclensionLookupTreeNode<NounDeclensionProducer, Void>(declension, _ -> declension);
		rootNode.putIfAbsent(declensionNode);
		
		return rootNode;
	}

	private static String plural(String stem, NounDeclensionKey key) {
		return stem + "s";
	}
}
