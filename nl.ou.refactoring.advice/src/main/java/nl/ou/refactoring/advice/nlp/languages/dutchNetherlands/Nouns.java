package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.Set;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionLookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionProducer;

class Nouns {
	private Nouns() { }
	
	static boolean isConsonant(char character) {
		return !isVowel(character);
	}
	
	static boolean isVowel(char character) {
		return "aeiouAEIOU".indexOf(character) != -1;
	}
	
	static NounDeclensionLookupTreeNode<Void, GrammaticalNumber> declensionDefaultTree() {
		final var rootNode = new NounDeclensionLookupTreeNode<Void, GrammaticalNumber>(Set.of(), null);
		
		// Singular
		final var singularNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, NounDeclensionProducer>(
				Set.of(GrammaticalNumber.SINGULAR),
				k -> k.number()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, ?, ?>)singularNode);
		NounDeclensionProducer singularDeclension = (s, _) -> s;
		final var singularDeclensionNode =
			new NounDeclensionLookupTreeNode<NounDeclensionProducer, Void>(
				Set.of(singularDeclension),
				_ -> singularDeclension
			);
		singularNode.putIfAbsent((LookupStemTreeNode<NounDeclensionKey, NounDeclensionProducer, ?, ?>)singularDeclensionNode);
		
		// Plural
		final var pluralNode =
			new NounDeclensionLookupTreeNode<GrammaticalNumber, NounDeclensionProducer>(
				Set.of(GrammaticalNumber.PLURAL),
				k -> k.number()
			);
		rootNode.putIfAbsent((LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, ?, ?>)pluralNode);
		NounDeclensionProducer pluralDeclension = (s, k) -> Nouns.plural(s, k);
		final var pluralDeclensionNode =
			new NounDeclensionLookupTreeNode<NounDeclensionProducer, Void>(
				Set.of(pluralDeclension),
				_ -> pluralDeclension // TODO modify stem or particle if necessary (e.g. vaat|, vat|en, slee|, slee|ën)
			);
		pluralNode.putIfAbsent((LookupStemTreeNode<NounDeclensionKey, NounDeclensionProducer, ?, ?>)pluralDeclensionNode);
		
		return rootNode;
	}
	
	static String plural(String stem, NounDeclensionKey key) {
		if (stem.length() > 3 &&
			!isVowel(stem.charAt(stem.length() - 3)) &&
			isVowel(stem.charAt(stem.length() - 2)) &&
			!isVowel(stem.charAt(stem.length() - 1))) {
			// If a noun ends in a CVC pattern, double the consonant, e.g. "stap" => "stappen"
			stem += stem.charAt(stem.length() - 1);
		} else if (stem.length() > 3 &&
			isVowel(stem.charAt(stem.length() - 3)) &&
			isVowel(stem.charAt(stem.length() - 2)) &&
			!isVowel(stem.charAt(stem.length() - 1))) {
			// If a noun ends in a VVC pattern, reduce the vowels, e.g. "schaap" => "schapen"
			stem = stem.substring(0, stem.length() - 3) + stem.substring(stem.length() - 2); 
		}
		stem += "en";
		return stem;
	}
}
