package nl.ou.refactoring.advice.nlp;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A node in a lookup tree with a stem value.
 * @param <Key> The type of lookup key.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the child nodes.
 * @param <ChildNodeType> The type of the node's child nodes.
 */
public class LookupStemTreeNode<Key, ValueType, ChildValueType, ChildNodeType extends LookupStemTreeNode<Key, ChildValueType, ?, ?>>
		extends LookupTreeNode<Key, ValueType, ChildValueType, ChildNodeType> {
	/**
	 * Initialises a new instance of {@link LookupStemTreeNode}.
	 * @param values The values that are matched by this node.
	 * @param valueProducer Produces a value from the specified key.
	 */
	public LookupStemTreeNode(Set<ValueType> values, Function<Key, ValueType> valueProducer) {
		super(values, valueProducer);
	}
	
	/**
	 * Gets the next child that matches the specified stem and lookup key.
	 * @param stem The stem of the lookup tree.
	 * @param key The lookup key.
	 * @return If found, the next tree node that matches the specified key wrapped in a {@link Optional}, otherwise an empty {@link Optional}.
	 */
	@SuppressWarnings("unchecked")
	public Optional<ChildNodeType> getChildNext(String stem, Key key) {
		ArgumentGuard.requireNotNull(key, "key");
		final var childEntries = this.children.entrySet();
		for (final var childEntry : childEntries) {
			final var childNode = childEntry.getValue();
			final var childNodeValues = childNode.getValues();
			if (childNodeValues != null &&
				(childNodeValues.size() == 0 ||
					childNodeValues.contains(childNode.valueProducer.apply(key)) ||
					(childNodeValues.size() == 1 && BiFunction.class.isInstance(childNodeValues.toArray()[0])))) {
				return Optional.of((ChildNodeType)childNode);
			}
		}
		return Optional.empty();
	}

	/**
	 * Puts the child node with the specified value, if it is not already present.
	 * @param <GrandchildValueType> The type of values of the child node's children.
	 * @param childNode The child node to put to this tree node.
	 * @return The child node with the specified value, identical to the childNode parameter's argument.
	 * @throws ArgumentNullException Thrown if childNode is null.
	 */
	public <GrandchildValueType> LookupStemTreeNode<Key, ChildValueType, GrandchildValueType, ?> putIfAbsent(LookupStemTreeNode<Key, ChildValueType, GrandchildValueType, ?> childNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(childNode, "childNode");
		final var childNodeValues = childNode.getValues();
		for (final var childNodeValue : childNodeValues) {
			this.children.putIfAbsent(childNodeValue, childNode);
		}
		return childNode;
	}
}
