package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.function.Function;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;

/**
 * A noun that is a reference.
 * @param <T> The type of entity that is being referred to.
 */
public final class NounReference<T> implements Noun {
	private final T reference;
	private final Function<T, String> captionFunction;
	
	/**
	 * Initialises a new instance of {@link NounReference}.
	 * @param reference The reference of the noun.
	 * @param captionFunction The function that provides the caption for the reference.
	 * @throws ArgumentNullException Thrown if reference is null.
	 */
	public NounReference(T reference, Function<T, String> captionFunction)
			throws ArgumentNullException {
		ArgumentGuard.requireNotNull(reference, "reference");
		ArgumentGuard.requireNotNull(captionFunction, "captionFunction");
		this.reference = reference;
		this.captionFunction = captionFunction;
	}
	
	/**
	 * Gets the caption for the reference.
	 * @return The caption for the reference.
	 */
	public String getCaption() {
		return this.captionFunction.apply(this.reference);
	}
	
	/**
	 * Gets the reference of the noun.
	 * @return The reference of the noun.
	 */
	public T getReference() {
		return this.reference;
	}
	
	@Override
	public String toString() {
		return this.getCaption();
	}
}