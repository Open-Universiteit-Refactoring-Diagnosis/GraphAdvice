package nl.ou.refactoring.advice.nlp.grammar.determiners;

import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.LookupTreeNode;

/**
 * A node in a look up tree for Possessive Pronoun declension.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the child nodes.
 */
public final class PronounPossessiveLookupTreeNode<ValueType, ChildValueType>
		extends LookupTreeNode<PronounPossessiveKey, ValueType, ChildValueType, LookupTreeNode<PronounPossessiveKey, ChildValueType, ?, ?>> {
	/**
	 * Initialises a new instance of {@link PronounPossessiveLookupTreeNode}.
 	 * @param value The value of the node.
	 * @param valueProducer A function that produces the value that is relevant for this node from the declension key.
	 */
	public PronounPossessiveLookupTreeNode(ValueType value, Function<PronounPossessiveKey, ValueType> valueProducer) {
		super(value, valueProducer);
	}
}
