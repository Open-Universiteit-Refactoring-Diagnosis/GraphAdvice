package nl.ou.refactoring.advice.nlp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A node in a lookup tree with a stem value.
 * @param <Key> The type of lookup key.
 * @param <ValueType> The type of value of the node.
 * @param <ChildValueType> The type of value of the child nodes.
 */
public final class LookupStemTreeNode<Key, ValueType, ChildValueType> {
	private final ValueType value;
	private final BiFunction<String, Key, String> valueProducer;
	private final Map<ChildValueType, LookupStemTreeNode<Key, ChildValueType, ?>> children =
		new HashMap<ChildValueType, LookupStemTreeNode<Key, ChildValueType, ?>>();

	/**
	 * Initialises a new instance of {@link LookupStemTreeNode<Key, ValueType, ChildValueType>}.
	 * @param value The value that is represented by this node.
	 * @param valueProducer Produces a value from the specified key.
	 */
	public LookupStemTreeNode(ValueType value, BiFunction<String, Key, String> valueProducer) {
		this.value = value;
		this.valueProducer = valueProducer;
	}
	
	/**
	 * Gets the requested child node, if present, wrapped in an {@link Optional},
	 * otherwise an empty {@link Optional}.
	 * @param value The value of the requested child node.
	 * @return The requested child node wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<LookupStemTreeNode<Key, ChildValueType, ?>> getChild(ChildValueType value) {
		return Optional.ofNullable(this.children.get(value));
	}
	
	/**
	 * Gets the next child that matches the specified stem and lookup key.
	 * @param stem The stem of the lookup tree.
	 * @param key The lookup key.
	 * @return If found, the next tree node that matches the specified key wrapped in a {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<LookupStemTreeNode<Key, ChildValueType, ?>> getChildNext(String stem, Key key) {
		ArgumentGuard.requireNotNull(key, "key");
		final var childEntries = this.children.entrySet();
		for (final var childEntry : childEntries) {
			final var childNode = childEntry.getValue();
			if (childNode.getValue() != null &&
					childNode.getValue().equals(childNode.produceValue(stem, key))) {
				return Optional.of(childNode);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the child nodes of this lookup tree node.
	 * @return An unmodifiable set of child {@link LookupStremTreeNode<Key, ChildValueType, ?>}.
	 */
	public Set<LookupStemTreeNode<Key, ChildValueType, ?>> getChildren() {
		return
			this
				.children
				.values()
				.stream()
				.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets the value that is represented by the node.
	 * @return The value that is represented by the node.
	 */
	public ValueType getValue() {
		return this.value;
	}
	
	/**
	 * Produces a value from the specified stem and key.
	 * @param stem The stem value of the lookup tree.
	 * @param key The lookup key.
	 * @return The produced value from the specified stem and key.
	 * @throws ArgumentNullException Thrown if stem or key is null.
	 * @throws ArgumentEmptyException Thrown if stem is empty or contains only white spaces.
	 */
	public String produceValue(String stem, Key key)
			throws
				ArgumentNullException,
				ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(stem, "stem");
		ArgumentGuard.requireNotNull(key, "key");
		return
			this.valueProducer == null
				? null
				: this.valueProducer.apply(stem, key);
	}

	/**
	 * Puts the child node with the specified value, if it is not already present.
	 * @param <GrandchildValueType> The type of values of the child node's children.
	 * @param childNode The child node to put to this tree node.
	 * @return The child node with the specified value, identical to childNode if not already present, otherwise the existing node.
	 * @throws ArgumentNullException Thrown if childNode is null.
	 */
	@SuppressWarnings("unchecked")
	public <GrandchildValueType> LookupStemTreeNode<Key, ChildValueType, GrandchildValueType> putIfAbsent(LookupStemTreeNode<Key, ChildValueType, GrandchildValueType> childNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(childNode, "childNode");
		return (LookupStemTreeNode<Key, ChildValueType, GrandchildValueType>)this.children.putIfAbsent(childNode.getValue(), childNode);
	}
}
