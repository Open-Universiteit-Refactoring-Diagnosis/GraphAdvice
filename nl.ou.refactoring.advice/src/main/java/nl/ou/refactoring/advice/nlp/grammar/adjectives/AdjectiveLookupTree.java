package nl.ou.refactoring.advice.nlp.grammar.adjectives;

import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupStemTree;
import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;

/**
 * A lookup tree for the declension of an Adjective in a Natural Language grammar.
 * @param <ValueType> The type of value of the first declension category.
 */
public final class AdjectiveLookupTree<ValueType>
		extends LookupStemTree<AdjectiveDeclensionKey, Void, ValueType> {

	public AdjectiveLookupTree(
		Supplier<String> stemSupplier,
		LookupStemTreeNode<AdjectiveDeclensionKey, Void, ValueType, ?> root
	) throws ArgumentNullException {
		super(stemSupplier, root);
	}
}
