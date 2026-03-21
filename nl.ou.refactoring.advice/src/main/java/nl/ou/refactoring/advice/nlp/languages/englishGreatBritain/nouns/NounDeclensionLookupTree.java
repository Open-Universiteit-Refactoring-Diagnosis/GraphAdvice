package nl.ou.refactoring.advice.nlp.languages.englishGreatBritain.nouns;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupStemTree;
import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalNumber;
import nl.ou.refactoring.advice.nlp.grammar.nouns.NounDeclensionKey;

public final class NounDeclensionLookupTree extends LookupStemTree<NounDeclensionKey, Void, GrammaticalNumber> {
	/**
	 * Initialises a new instance of {@link NounDeclensionLookupTree}.
	 * @param stem The stem of the Noun.
	 * @throws ArgumentNullException Thrown if stem is null.
	 * @throws ArgumentEmptyException Thrown if stem is empty or contains only white spaces.
	 */
	public NounDeclensionLookupTree(String stem)
			throws ArgumentNullException, ArgumentEmptyException {
		super(stem, constructTree());
	}
	
	private static LookupStemTreeNode<NounDeclensionKey, Void, GrammaticalNumber> constructTree() {
		final var rootNode = new LookupStemTreeNode<NounDeclensionKey, Void, GrammaticalNumber>(null, null);
		
		// Singular
		final var singularNode =
			new LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, String>(
				GrammaticalNumber.SINGULAR,
				k -> k.number()
			);
		rootNode.putIfAbsent(singularNode);
		
		// Plural
		final var pluralNode =
			new LookupStemTreeNode<NounDeclensionKey, GrammaticalNumber, String>(
				GrammaticalNumber.PLURAL,
				k -> k.number()
			);
		rootNode.putIfAbsent(pluralNode);
		
		return rootNode;
	}
}