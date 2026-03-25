package nl.ou.refactoring.advice.nlp;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A node in a lookup tree with a stem value.
 * @param <Key> The type of lookup key.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the child nodes.
 */
public class LookupStemTreeNode<Key, ValueType, ChildValueType, ChildNodeType extends LookupStemTreeNode<Key, ChildValueType, ?, ?>>
		extends LookupTreeNode<Key, ValueType, ChildValueType, ChildNodeType> {
	/**
	 * Initialises a new instance of {@link LookupStemTreeNode<Key, ValueType, ChildValueType>}.
	 * @param value The value that is represented by this node.
	 * @param valueProducer Produces a value from the specified key.
	 */
	public LookupStemTreeNode(ValueType value, Function<Key, ValueType> valueProducer) {
		super(value, valueProducer);
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
			final var childNodeValue = childNode.getValue();
			if (childNodeValue != null &&
				(childNodeValue.equals(childNode.valueProducer.apply(key)) ||
					BiFunction.class.isInstance(childNodeValue))) {
				return Optional.of((ChildNodeType)childNode);
			}
		}
		return Optional.empty();
	}

	/**
	 * Puts the child node with the specified value, if it is not already present.
	 * @param <GrandchildValueType> The type of values of the child node's children.
	 * @param childNode The child node to put to this tree node.
	 * @return The child node with the specified value, identical to childNode if not already present, otherwise the existing node.
	 * @throws ArgumentNullException Thrown if childNode is null.
	 */
	@SuppressWarnings("unchecked")
	public <GrandchildValueType> LookupStemTreeNode<Key, ChildValueType, GrandchildValueType, ?> putIfAbsent(LookupStemTreeNode<Key, ChildValueType, GrandchildValueType, ?> childNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(childNode, "childNode");
		return (LookupStemTreeNode<Key, ChildValueType, GrandchildValueType, ?>)this.children.putIfAbsent(childNode.getValue(), childNode);
	}
}
