package nl.ou.refactoring.advice.nlp.grammar.nouns;

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
	 * @param value The value of the node.
	 * @param valueProducer Produces a value from the lookup key that corresponds to the value of the node.
	 */
	public NounDeclensionLookupTreeNode(ValueType value, Function<NounDeclensionKey, ValueType> valueProducer) {
		super(value, valueProducer);
	}
}