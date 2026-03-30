package nl.ou.refactoring.advice.nlp;

import java.util.Optional;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

public class LookupTree<Key, ValueType, ChildValueType, NodeType extends LookupTreeNode<Key, ValueType, ChildValueType, ?>> {
	protected final NodeType root;
	
	/**
	 * Initialises a new instance of {@link LookupTree}.
	 * @param root The root node of the lookup tree.
	 * @throws ArgumentNullException Thrown if root is null.
	 */
	public LookupTree(NodeType root)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(root, "root");
		this.root = root;
	}
	
	/**
	 * Gets the root node of the lookup tree.
	 * @return The root node of the lookup tree.
	 */
	public NodeType getRoot() {
		return this.root;
	}
	
	/**
	 * Looks up a value in the lookup tree with the specified key.
	 * @param key The specified key to look up.
	 * @return The matching {@link String} value wrapped in an {@link Optional}, if not found an empty {@link Optional}.
	 */
	@SuppressWarnings("unchecked")
	public Optional<String> lookup(Key key) {
		Optional<NodeType> nodeCurrent = Optional.of(this.root);
		Optional<String> matchingValue = Optional.empty();
		do {
			final var node = nodeCurrent.get();
			final var nodeNext = node.getChildNext(key);
			if (nodeNext.isPresent()) {
				nodeCurrent = Optional.of((NodeType)nodeNext.get());
			} else {
				final var nodeValues = node.getValues();
				if (nodeValues.size() == 1 && String.class.isInstance(nodeValues.toArray()[0])) {
					return Optional.of((String)nodeValues.toArray()[0]);
				}
				nodeCurrent = Optional.empty();
			}
		} while (nodeCurrent.isPresent());
		return matchingValue;
	}
}