package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupStemTree;

/**
 * A look up tree for Noun declension in the Dutch language (Netherlands).
 * @param <ValueType> The type of value of the first declension category.
 */
public final class NounDeclensionLookupTree<ValueType>
		extends LookupStemTree<NounDeclensionKey, Void, ValueType> {
	/**
	 * Initialises a new instance of {@link stem}.
	 * @param stemSupplier The stem of the Noun.
	 * @param root The root node of the lookup tree.
	 * @throws ArgumentNullException Thrown if stemSupplier is null.
	 */
	public NounDeclensionLookupTree(Supplier<String> stemSupplier, NounDeclensionLookupTreeNode<Void, ValueType> root)
			throws ArgumentNullException {
		super(stemSupplier, root);
	}
}