package nl.ou.refactoring.advice.nlp.grammar.verbs;

import java.util.Set;
import java.util.function.Function;

import nl.ou.refactoring.advice.nlp.LookupStemTreeNode;

/**
 * A node in a lookup tree for Verb conjugation in a Natural Language grammar.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the node's child nodes.
 */
public final class VerbConjugationLookupTreeNode<ValueType, ChildValueType>
		extends LookupStemTreeNode<VerbConjugationKey, ValueType, ChildValueType, VerbConjugationLookupTreeNode<ChildValueType, ?>> {
	/**
	 * Initialises a new instance of {@link VerbConjugationLookupTreeNode}.
	 * @param values The values of the node.
	 * @param valueProducer A function that produces a value from the conjugation key that corresponds to the node's value.
	 */
	public VerbConjugationLookupTreeNode(Set<ValueType> values, Function<VerbConjugationKey, ValueType> valueProducer) {
		super(values, valueProducer);
	}
}