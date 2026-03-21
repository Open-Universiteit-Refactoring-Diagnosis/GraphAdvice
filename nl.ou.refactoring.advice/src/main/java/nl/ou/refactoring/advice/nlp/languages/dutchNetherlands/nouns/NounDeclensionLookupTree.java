package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands.nouns;

import java.util.function.BiFunction;

import nl.ou.refactoring.advice.nlp.LookupStemTree;
import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;

/**
 * A look up tree for Noun declension in the Dutch language (Netherlands).
 */
public final class NounDeclensionLookupTree extends LookupStemTree<NounDeclensionKey, Void, GrammaticalNumber> {
	/**
	 * Initialises a new instance of {@link stem}.
	 * @param stem The stem of the Noun.
	 */
	public NounDeclensionLookupTree(String stem) {
		super(stem, constructTree());
	}
	
	private static LookupStemTreeNode<NounDeclensionKey, Void, GrammaticalNumber> constructTree() {
		final var rootNode = new LookupStemTreeNode<NounDeclensionKey, Void, GrammaticalNumber>(null, null);
		
		// Singular
		final var singularNode =
			new LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, BiFunction<String, NounDeclensionKey, String>>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		rootNode.putIfAbsent(singularNode);
		BiFunction<String, NounDeclensionKey, String> singularDeclension = (s, _) -> s;
		final var singularDeclensionNode =
			new LookupStemTreeNode<NounDeclensionKey, BiFunction<String, NounDeclensionKey, String>, Void>(
				singularDeclension,
				_ -> singularDeclension
			);
		singularNode.putIfAbsent(singularDeclensionNode);
		
		// Plural
		final var pluralNode =
			new LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, BiFunction<String, NounDeclensionKey, String>>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		rootNode.putIfAbsent(pluralNode);
		BiFunction<String, NounDeclensionKey, String> pluralDeclension = (s, _) -> s + "en";
		final var pluralDeclensionNode =
			new LookupStemTreeNode<NounDeclensionKey, BiFunction<String, NounDeclensionKey, String>, Void>(
				pluralDeclension,
				_ -> pluralDeclension // TODO modify stem or particle if necessary (e.g. vaat|, vat|en, slee|, slee|ën)
			);
		pluralNode.putIfAbsent(pluralDeclensionNode);
		
		return rootNode;
	}
}