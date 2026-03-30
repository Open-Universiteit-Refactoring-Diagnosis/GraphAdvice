package nl.ou.refactoring.advice.nlp;

import java.util.Collections;
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
public class LookupTreeNode<Key, ValueType, ChildValueType, ChildNodeType extends LookupTreeNode<Key, ChildValueType, ?, ?>> {
	protected final Set<ValueType> values;
	protected final Function<Key, ValueType> valueProducer;
	protected final Map<ChildValueType, LookupTreeNode<Key, ?, ?, ?>> children =
		new HashMap<ChildValueType, LookupTreeNode<Key, ?, ?, ?>>();
	
	/**
	 * Initialises a new instance of {@link LookupTreeNode<Key, ValueType, ChildValueType>}.
	 * @param values The values that are matched by this node.
	 * @param valueProducer Produces a value from the specified key.
	 * @throws ArgumentNullException Thrown if values is null.
	 */
	public LookupTreeNode(Set<ValueType> values, Function<Key, ValueType> valueProducer)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(values, "values");
		this.values = values;
		this.valueProducer = valueProducer;
	}
	
	/**
	 * Gets the requested child node, if present, wrapped in an {@link Optional},
	 * otherwise an empty {@link Optional}.
	 * @param value The value of the requested child node.
	 * @return The requested child node wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	@SuppressWarnings("unchecked")
	public final Optional<ChildNodeType> getChild(ChildValueType value) {
		return Optional.ofNullable((ChildNodeType)this.children.get(value));
	}
	
	/**
	 * Gets the next child node that matches the specified lookup key.
	 * @param key The key that provides the parameters to look for when looking up the requested value.
	 * @return If found, the next tree node that matches the specified key wrapped in a {@link Optional}, otherwise an empty {@link Optional}.
	 */
	@SuppressWarnings("unchecked")
	public Optional<ChildNodeType> getChildNext(Key key) {
		ArgumentGuard.requireNotNull(key, "key");
		final var childEntries = this.children.entrySet();
		for (final var childEntry : childEntries) {
			final var childNode = childEntry.getValue();
			if (childNode.getValues() != null &&
					childNode.getValues().contains(childNode.produceValue(key))) {
				return Optional.of((ChildNodeType)childNode);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the child nodes of this lookup tree node.
	 * @return An unmodifiable set of child {@link LookupTreeNode<Key, ChildValueType, ?>}s.
	 */
	@SuppressWarnings("unchecked")
	public final Set<ChildNodeType> getChildren() {
		return
			this
				.children
				.values()
				.stream()
				.map((node) -> (ChildNodeType)node)
				.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets the values that are matched by the node.
	 * @return An unmodifiable set of values that are matched by the node.
	 */
	public final Set<ValueType> getValues() {
		return Collections.unmodifiableSet(this.values);
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
	 * @return The child node with the specified value, identical to the childNode parameter's argument.
	 * @throws ArgumentNullException Thrown if childNode is null.
	 */
	public final <GrandchildValueType> ChildNodeType putIfAbsent(ChildNodeType childNode)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(childNode, "childNode");
		final var childNodeValues = childNode.getValues();
		for (final var childNodeValue : childNodeValues) {
			this.children.putIfAbsent(childNodeValue, childNode);
		}
		return childNode;
	}
}
