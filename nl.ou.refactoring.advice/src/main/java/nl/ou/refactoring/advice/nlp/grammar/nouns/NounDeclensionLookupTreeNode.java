package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Set;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;

/**
 * A node in a look up tree for declension of Nouns in a Natural Language grammar.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the node's child nodes.
 */
public final class NounDeclensionLookupTreeNode<ValueType, ChildValueType>
		extends LookupStemTreeNode<NounDeclensionKey, ValueType, ChildValueType, NounDeclensionLookupTreeNode<ChildValueType, ?>> {
	/**
	 * Initialises a new instance of {@link NounDeclensionLookupTreeNode}.
	 * @param values The values that match the node.
	 * @param valueProducer Produces a value from the lookup key that corresponds to the value of the node.
	 */
	public NounDeclensionLookupTreeNode(Set<ValueType> values, Function<NounDeclensionKey, ValueType> valueProducer) {
		super(values, valueProducer);
	}
}