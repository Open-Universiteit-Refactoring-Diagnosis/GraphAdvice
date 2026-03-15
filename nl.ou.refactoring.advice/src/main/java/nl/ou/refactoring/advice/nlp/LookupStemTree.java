package nl.ou.refactoring.advice.nlp;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentEmptyException;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A lookup tree with a stem.
 * @param <Key> The type of lookup key.
 */
public class LookupStemTree<Key, ValueType, ChildValueType> {
	private final String stem;
	private final LookupStemTreeNode<Key, ValueType, ChildValueType> root;
	
	/**
	 * Initialises a new instance of {@link LookupStemTree}.
	 * @param stem The stem value of the lookup tree.
	 * @param root The root node of the lookup tree.
	 * @throws ArgumentNullException Thrown if stem or root is null.
	 * @throws ArgumentEmptyException Thrown if stem is empty or contains only white spaces.
	 */
	public LookupStemTree(String stem, LookupStemTreeNode<Key, ValueType, ChildValueType> root)
			throws ArgumentNullException, ArgumentEmptyException {
		ArgumentGuard.requireNotNullEmptyOrWhiteSpace(stem, "stem");
		ArgumentGuard.requireNotNull(root, "root");
		this.stem = stem;
		this.root = root;
	}

	/**
	 * Gets the stem value of the lookup tree.
	 * @return The stem value of the lookup tree.
	 */
	public String getStem() {
		return this.stem;
	}
	
	/**
	 * Gets the root node of the lookup tree.
	 * @return The root node of the lookup tree.
	 */
	public LookupStemTreeNode<Key, ValueType, ChildValueType> getRoot() {
		return this.root;
	}
	
	/**
	 * Looks up a value in the lookup tree with the specified key and stem of this tree.
	 * @param key The specified key to look up.
	 * @return The matching {@link String} value wrapped in an {@link Optional}, if not found an empty {@link Optional}.
	 */
	public Optional<String> lookup(Key key) {
		Optional<LookupStemTreeNode<Key, ?, ?>> nodeCurrent = Optional.of(this.root);
		Optional<String> matchingValue = Optional.empty();
		do {
			final var node = nodeCurrent.get();
			final var nodeNext = node.getChildNext(this.stem, key);
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
