package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.LookupStemTree;
import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;

/**
 * A lookup tree for Verb conjugation in a Natural Language grammar.
 * @param <ValueType> The type of value of the root node.
 * @param <ChildValueType> The type of value of the root node's child nodes.
 */
public final class VerbConjugationLookupTree<ValueType>
		extends LookupStemTree<VerbConjugationKey, Void, ValueType> {
	/**
	 * Initialises a new instance of {@link VerbConjugationTree}.
	 * @param stemSupplier Supplies the stem of the verb.
	 * @param root The root of the lookup tree.
	 * @throws ArgumentNullException Thrown if stemSupplier or root is null.
	 */
	public VerbConjugationLookupTree
	(
		Supplier<String> stemSupplier,
		LookupStemTreeNode<VerbConjugationKey, Void, ValueType, ?> root
	) throws ArgumentNullException {
		super(stemSupplier, root);
	}
}