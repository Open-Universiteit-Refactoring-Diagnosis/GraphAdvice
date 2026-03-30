package nl.ou.refactoring.advice.nlp;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A lookup tree with a stem.
 * @param <Key> The type of lookup key.
 */
public class LookupStemTree<Key, ValueType, ChildValueType>
		extends LookupTree<Key, ValueType, ChildValueType, LookupStemTreeNode<Key, ValueType, ChildValueType, ?>> {
	private final Supplier<String> stem;
	
	/**
	 * Initialises a new instance of {@link LookupStemTree}.
	 * @param stemSupplier Supplies the stem value of the lookup tree.
	 * @param root The root node of the lookup tree.
	 * @throws ArgumentNullException Thrown if stem or root is null.
	 */
	public LookupStemTree(Supplier<String> stemSupplier, LookupStemTreeNode<Key, ValueType, ChildValueType, ?> root)
			throws ArgumentNullException {
		super(root);
		ArgumentGuard.requireNotNull(stemSupplier, "stemSupplier");
		this.stem = stemSupplier;
	}

	/**
	 * Gets the stem value of the lookup tree.
	 * @return The stem value of the lookup tree.
	 */
	public String getStem() {
		return this.stem.get();
	}
	
	/**
	 * Looks up a value in the lookup tree with the specified key and stem of this tree.
	 * @param key The specified key to look up.
	 * @return The matching {@link String} value wrapped in an {@link Optional}, if not found an empty {@link Optional}.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Optional<String> lookup(Key key) {
		Optional<LookupStemTreeNode<Key, ?, ?, ?>> nodeCurrent = Optional.of(this.root);
		Optional<String> matchingValue = Optional.empty();
		do {
			final var node = nodeCurrent.get();
			final var nodeNext = node.getChildNext(this.stem.get(), key);
			if (nodeNext.isPresent()) {
				nodeCurrent = Optional.of(nodeNext.get());
			} else {
				final var nodeValues = node.getValues();
				if (nodeValues.size() == 1 && BiFunction.class.isInstance(nodeValues.toArray()[0])) {
					return Optional.of(((BiFunction<String, Key, String>)nodeValues.toArray()[0]).apply(this.stem.get(), key));
				}
				nodeCurrent = Optional.empty();
			}
		} while (nodeCurrent.isPresent());
		return matchingValue;
	}
}
