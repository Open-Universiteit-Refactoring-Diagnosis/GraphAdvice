package nl.ou.refactoring.advice.nlp.tokens;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A store for tokens and associated syntax elements.
 * @param <T> The type of syntax element associated with the tokens.
 */
public final class TokenStore<T> {
	private final Map<Long, T> syntaxElements;

	/**
	 * Initialises a new instance of {@link TokenStore}.
	 */
	protected TokenStore() {
		this.syntaxElements = new HashMap<Long, T>();
	}
	
	/**
	 * Gets all registered tokens in this {@link TokenStore}.
	 * @return An unmodifiable set of tokens.
	 */
	public Set<Long> allTokens() {
		return Collections.unmodifiableSet(this.syntaxElements.keySet());
	}

	/**
	 * Gets the syntax element associated with the token.
	 * @param token The token for which to retrieve the syntax element.
	 * @return If found, the syntax element wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 */
	public Optional<T> get(long token) {
		return Optional.ofNullable(this.syntaxElements.get(token));
	}
	
	/**
	 * Puts the specified item and associates it with the specified token, if not already present in the store.
	 * @param token The token to associate the specified item with.
	 * @param item The item to put in the store.
	 * @return The item that is put in the store.
	 */
	public T putIfAbsent(long token, T item) {
		return this.syntaxElements.putIfAbsent(token, item);
	}
}
