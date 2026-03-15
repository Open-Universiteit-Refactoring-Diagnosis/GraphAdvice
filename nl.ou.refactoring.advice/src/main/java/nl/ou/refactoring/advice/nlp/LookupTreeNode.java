package nl.ou.refactoring.advice.nlp;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * Represents a node in a lookup tree for looking up values for tokens.
 * @param <ValueType> The type of value represented by the node.
 */
public class LookupTreeNode<Key, ValueType, ChildValueType> {
	private final ValueType value;
	private final Function<Key, ValueType> valueProducer;
	private final Map<ChildValueType, LookupTreeNode<Key, ChildValueType, ?>> children =
		new HashMap<ChildValueType, LookupTreeNode<Key, ChildValueType, ?>>();
	
	/**
	 * Initialises a new instance of {@link LookupTreeNode<Key, ValueType, ChildValueType>}.
	 * @param value The value that is represented by this node.
	 * @param valueProducer Produces a value from the specified key.
	 */
	public LookupTreeNode(ValueType value, Function<Key, ValueType> valueProducer) {
		this.value = value;
		this.valueProducer = valueProducer;
	}
	
	/**
	 * Gets the requested child node, if present, wrapped in an {@link Optional},
	 * otherwise an empty {@link Optional}.
	 * @param value The value of the requested child node.
	 * @return The requested child node wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public final Optional<LookupTreeNode<Key, ChildValueType, ?>> getChild(ChildValueType value) {
		return Optional.ofNullable(this.children.get(value));
	}
	
	/**
	 * Gets the next child node that matches the specified lookup key.
	 * @param key The key that provides the parameters to look for when looking up the requested value.
	 * @return If found, the next tree node that matches the specified key wrapped in a {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public final Optional<LookupTreeNode<Key, ChildValueType, ?>> getChildNext(Key key) {
		ArgumentGuard.requireNotNull(key, "key");
		final var childEntries = this.children.entrySet();
		for (final var childEntry : childEntries) {
			final var childNode = childEntry.getValue();
			if (childNode.getValue() != null &&
					childNode.getValue().equals(childNode.produceValue(key))) {
				return Optional.of(childNode);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the child nodes of this lookup tree node.
	 * @return An unmodifiable set of child {@link LookupTreeNode<Key, ChildValueType, ?>}s.
	 */
	public final Set<LookupTreeNode<Key, ChildValueType, ?>> getChildren() {
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
	public final ValueType getValue() {
		return this.value;
	}
	
	/**
	 * Gets a value from a lookup key.
	 * @param key The lookup key.
	 * @return The value from the lookup key.
	 * @throws ArgumentNullException Thrown if key is null.
	 */
	public final ValueType produceValue(Key key)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(key, "key");
		return
			this.valueProducer == null
				? null
				: this.valueProducer.apply(key);
	}
	
	/**
	 * Puts the child node with the specified value, if it is not already present.
	 * @param <GrandchildValueType> The type of values of the child node's children.
	 * @param childNode The child node to put to this tree node.
	 * @return The child node with the specified value, identical to childNode if not already present, otherwise the existing node.
	 * @throws ArgumentNullException Thrown if childNode is null.
	 */
	@SuppressWarnings("unchecked")
	public final <GrandchildValueType> LookupTreeNode<Key, ChildValueType, GrandchildValueType> putIfAbsent(LookupTreeNode<Key, ChildValueType, GrandchildValueType> childNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(childNode, "childNode");
		return (LookupTreeNode<Key, ChildValueType, GrandchildValueType>)this.children.putIfAbsent(childNode.getValue(), childNode);
	}
}
