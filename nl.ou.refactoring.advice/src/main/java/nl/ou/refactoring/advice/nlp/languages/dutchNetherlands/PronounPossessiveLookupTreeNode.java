package nl.ou.refactoring.advice.nlp.languages.dutchNetherlands;

import java.util.Set;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.LookupTreeNode;
import nl.ou.refactoring.advice.nlp.grammar.determiners.PronounPossessiveKey;

/**
 * A node in a look up tree for Possessive Pronoun declension.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the child nodes.
 */
public final class PronounPossessiveLookupTreeNode<ValueType, ChildValueType>
		extends LookupTreeNode<PronounPossessiveKey, ValueType, ChildValueType, LookupTreeNode<PronounPossessiveKey, ChildValueType, ?, ?>> {
	/**
	 * Initialises a new instance of {@link PronounPossessiveLookupTreeNode}.
 	 * @param values The values that are matched by the node.
	 * @param valueProducer A function that produces the value that is relevant for this node from the declension key.
	 */
	public PronounPossessiveLookupTreeNode(Set<ValueType> values, Function<PronounPossessiveKey, ValueType> valueProducer) {
		super(values, valueProducer);
	}
}
