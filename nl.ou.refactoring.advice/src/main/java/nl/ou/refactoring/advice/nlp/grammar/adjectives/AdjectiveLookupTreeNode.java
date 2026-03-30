package nl.ou.refactoring.advice.nlp.grammar.adjectives;

import java.util.Set;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;

/**
 * A node in a lookup tree for the declension of Adjectives in a Natural Language grammar.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the node's child nodes.
 */
public final class AdjectiveLookupTreeNode<ValueType, ChildValueType>
		extends LookupStemTreeNode<AdjectiveDeclensionKey, ValueType, ChildValueType, AdjectiveLookupTreeNode<ChildValueType, ?>> {
	/**
	 * Initialises a new instance of {@link AdjectiveLookupTreeNode}.
	 * @param values The values that are matched by the node.
	 * @param valueProducer A function that produces a value from the declension key that corresponds to the value of the node.
	 */
	public AdjectiveLookupTreeNode(Set<ValueType> values, Function<AdjectiveDeclensionKey, ValueType> valueProducer) {
		super(values, valueProducer);
	}
}