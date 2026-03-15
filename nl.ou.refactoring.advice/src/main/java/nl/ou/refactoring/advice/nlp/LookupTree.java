package nl.ou.refactoring.advice.nlp;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

public class LookupTree<Key, ValueType, ChildValueType> {
	private final LookupTreeNode<Key, ValueType, ChildValueType> root;
	
	/**
	 * Initialises a new instance of {@link LookupTree}.
	 * @param root The root node of the lookup tree.
	 * @throws ArgumentNullException Thrown if root is null.
	 */
	public LookupTree(LookupTreeNode<Key, ValueType, ChildValueType> root)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(root, "root");
		this.root = root;
	}
	
	/**
	 * Gets the root node of the lookup tree.
	 * @return The root node of the lookup tree.
	 */
	public LookupTreeNode<Key, ValueType, ChildValueType> getRoot() {
		return this.root;
	}
	
	/**
	 * Looks up a value in the lookup tree with the specified key.
	 * @param key The specified key to look up.
	 * @return The matching {@link String} value wrapped in an {@link Optional}, if not found an empty {@link Optional}.
	 */
	public Optional<String> lookup(Key key) {
		Optional<LookupTreeNode<Key, ?, ?>> nodeCurrent = Optional.of(this.root);
		Optional<String> matchingValue = Optional.empty();
		do {
			final var node = nodeCurrent.get();
			final var nodeNext = node.getChildNext(key);
			if (nodeNext.isPresent()) {
				nodeCurrent = Optional.of(nodeNext.get());
			} else {
				if (String.class.isInstance(node.getValue())) {
					return Optional.of((String)node.getValue());
				}
				nodeCurrent = Optional.empty();
			}
		} while (nodeCurrent.isPresent());
		return matchingValue;
	}
}