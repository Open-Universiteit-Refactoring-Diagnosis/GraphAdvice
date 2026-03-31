package nl.ou.refactoring.advice.nlp.grammar.nouns;

import java.util.Optional;
import java.util.function.Supplier;

import nl.ou.refactoring.advice.contracts.ArgumentGuard;
import nl.ou.refactoring.advice.contracts.ArgumentNullException;
import nl.ou.refactoring.advice.nlp.grammar.GrammaticalGender;
import nl.ou.refactoring.advice.nlp.grammar.SyntaxElement;

/**
 * Represents a Noun in Natural Language.
 */
public abstract class Noun
		implements
			Cloneable,
			SyntaxElement {
	/**
	 * The token of the {@link Noun}.
	 */
	private final long token;
	
	/**
	 * The grammatical {@link NounCategories} of this {@link Noun}.
	 */
	protected final NounCategories categories;
	
	/**
	 * Initialises a new instance of {@link Noun}.
	 * @param token The token of the {@link Noun}.
	 * @param categories The categories of the {@link Noun}.
	 * @throws ArgumentNullException Thrown if categories is null.
	 */
	protected Noun(
		long token,
		NounCategories categories
	) throws ArgumentNullException {
		ArgumentGuard.requireNotNull(categories, "categories");
		this.token = token;
		this.categories = categories;
	}
	
	/**
	 * Gets the token that represents the {@link Noun}.
	 * @return The token that represents the {@link Noun}.
	 */
	public final long getToken() {
		return this.token;
	}
	
	/**
	 * Gets the {@link LexicalCategory} of the {@link Noun}.
	 * @return The {@link LexicalCategory} of the {@link Noun}.
	 */
	public final LexicalCategory getLexicalCategory() {
		return this.categories.lexicalCategory();
	}
	
	/**
	 * Gets the {@link SemanticClassification} of the {@link Noun}.
	 * @return The {@link SemanticClassification} of the {@link Noun}.
	 */
	public SemanticClassification getSemanticClassification() {
		return this.categories.semanticClassification();
	}
	
	/**
	 * Gets the {@link Countability} of the {@link Noun}.
	 * @return The {@link Countability} of the {@link Noun}.
	 */
	public Countability getCountability() {
		return this.categories.countability();
	}
	
	/**
	 * Gets the {@link GrammaticalGender} of the {@link Noun}.
	 * Each {@link Noun} class may have a different way of determining the gender.
	 * For example, a {@link Pronoun}'s gender is intrinsic, a {@link CommonNoun}'s gender is determined by the language.
	 * @param genderSupplier Supplies the {@link GrammaticalGender} of the {@link Noun}.
	 * @return The {@link GrammaticalGender} of the {@link Noun} if found, wrapped in an {@link Optional}, otherwise an empty {@link Optional}.
	 * @throws ArgumentNullException Thrown if genderSupplier is null.
	 */
	public abstract Optional<GrammaticalGender> getGender(Supplier<GrammaticalGender> genderSupplier)
		throws ArgumentNullException;
	
	@Override
	protected abstract Object clone();
}