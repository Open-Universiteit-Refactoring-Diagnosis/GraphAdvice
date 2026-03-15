package nl.ou.refactoring.advice.nlp.tokens;

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
 * @param <T> The type of value represented by the node.
 */
public final class TokensLookupTreeNode<K, T, U> {
	private final T value;
	private final Function<K, T> valueGetter;
	private final Map<U, TokensLookupTreeNode<K, U, ?>> children =
		new HashMap<U, TokensLookupTreeNode<K, U, ?>>();
	
	/**
	 * Initialises a new instance of {@link TokensLookupTreeNode<T>}.
	 * @param value The value that is represented by this node.
	 */
	public TokensLookupTreeNode(T value, Function<K, T> valueGetter) {
		this.value = value;
		this.valueGetter = valueGetter;
	}
	
	/**
	 * Gets the requested child, if present, wrapped in an {@link Optional<TokensLookupTreeNode<U, ?>>},
	 * otherwise an empty {@link Optional<TokensLookupTreeNode<U, ?>>}.
	 * @param value The value of the requested child node.
	 * @return The requested child wrapped in an {@link Optional<TokensLookupTreeNode<U, ?>>}, otherwise an empty {@link Optional<TokensLookupTreeNode<U, ?>>}.
	 */
	public Optional<TokensLookupTreeNode<K, U, ?>> getChild(U value) {
		return Optional.ofNullable(this.children.get(value));
	}
	
	/**
	 * Gets the next child that matches the specified lookup key.
	 * @param key The key that provides the parameters to look for when looking up the requested value.
	 * @return If found, the next tree node that matches the specified key wrapped in a {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<TokensLookupTreeNode<K, U, ?>> getChildNext(K key) {
		ArgumentGuard.requireNotNull(key, "key");
		final var childEntries = this.children.entrySet();
		for (final var childEntry : childEntries) {
			final var childNode = childEntry.getValue();
			if (childNode.getValue() != null &&
					childNode.getValue().equals(childNode.getValueFromKey(key))) {
				return Optional.of(childNode);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the child nodes of this lookup tree node.
	 * @return An unmodifiable set of child {@link TokensLookupTreeNode<U, ?>}s.
	 */
	public Set<TokensLookupTreeNode<K, U, ?>> getChildren() {
		return
			this
				.children
				.values()
				.stream()
				.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Gets the value that is represented by the node.
	 * @return The value that is represented by the node.s
	 */
	public T getValue() {
		return this.value;
	}
	
	/**
	 * Gets a value from a lookup key.
	 * @param key The lookup key.
	 * @return The value from the lookup key.
	 * @throws ArgumentNullException Thrown if key is null.
	 */
	public T getValueFromKey(K key)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(key, "key");
		return
			this.valueGetter == null
				? null
				: this.valueGetter.apply(key);
	}
	
	/**
	 * Puts the child node with the specified value, if it is not already present.
	 * @param <V> The type of values of the child node's children.
	 * @param childNode The child node to put to this tree node.
	 * @return The child node with the specified value, identical to childNode if not already present, otherwise the existing node.
	 */
	@SuppressWarnings("unchecked")
	public <V> TokensLookupTreeNode<K, U, V> putIfAbsent(TokensLookupTreeNode<K, U, V> childNode) {
		return (TokensLookupTreeNode<K, U, V>)this.children.putIfAbsent(childNode.getValue(), childNode);
	}
}
