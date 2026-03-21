package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.function.Function;
import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.tokens.Tokens;

/**
 * A noun that is a reference to an object within Refactoring Advice.
 * @param <T> The type of entity that is being referred to.
 */
public final class ReferenceNoun<T> extends Noun {
	public static long TOKEN = Tokens.Nouns.REFERENCE;
	private final T reference;
	private final Function<T, String> captionFunction;
	
	/**
	 * Initialises a new instance of {@link ReferenceNoun}.
	 * @param reference The reference of the Noun.
	 * @param captionFunction The function that provides the caption for the reference.
	 * @param gender The Grammatical Gender of the Noun.
	 * @throws ArgumentNullException Thrown if reference is null.
	 */
	public ReferenceNoun(T reference, Function<T, String> captionFunction, GrammaticalGender gender)
			throws ArgumentNullException {
		super(TOKEN, LexicalCategory.PROPER, SemanticClassification.ABSTRACT, Countability.UNCOUNTABLE, () -> gender);
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
	protected Object clone() {
		return new ReferenceNoun<T>(this.reference, this.captionFunction, this.getGender());
	}
	
	@Override
	public String toString() {
		return this.getCaption();
	}
}